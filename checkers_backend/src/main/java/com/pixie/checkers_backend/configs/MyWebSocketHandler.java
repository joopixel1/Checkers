package com.pixie.checkers_backend.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixie.checkers_backend.annotations.*;
import com.pixie.checkers_backend.models.modals.MessageModal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.util.ReflectionUtils.invokeMethod;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyWebSocketHandler implements WebSocketHandler {

    private final ApplicationContext applicationContext;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HashMap<String, List<WebSocketSession>> topics = new HashMap<>();
    private static final HashMap<WebSocketSession, List<String>> sessions = new HashMap<>();

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(msg -> {
                    try {
                        TypeReference<MessageModal> typeReference = new TypeReference<MessageModal>(){};
                        return Mono.just(objectMapper.readValue(msg, typeReference));
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        return Mono.error(e);
                    }
                })
                .flatMap(modal -> handleModal(session, modal))
                .then(Mono.empty());
    }

    private Mono<String> handleModal(WebSocketSession session, MessageModal modal){
        handleTypes(session, modal);
        Map<MessageModal.MessageType, Class<? extends Annotation>> annotationMap = Map.of(
                MessageModal.MessageType.PUBLISH, PublishMapping.class,
                MessageModal.MessageType.SUBSCRIBE, SubscribeMapping.class,
                MessageModal.MessageType.UNSUBSCRIBE, UnSubscribeMapping.class
        );
        return findMethodByAnnotation(modal.getPath(), annotationMap.get(modal.getType()))
                .flatMap(pair -> {
                    try {
                        Mono<?> result = (Mono<?>) ReflectionUtils.invokeMethod(pair.getSecond(), pair.getFirst(), resolveMethodArguments(pair.getSecond(), modal, session));
                        assert result != null;
                        return result.doOnSubscribe(successResult -> broadcast(modal.getTopic(), result))
                                .flatMap(successResult -> Mono.just(""));
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return Mono.error(new RuntimeException(e));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Handler not found for path: " + modal.getPath())));
    }

    private void handleTypes(WebSocketSession session, MessageModal modal) {
        if (modal.getType() == MessageModal.MessageType.SUBSCRIBE) {
            if (!topics.containsKey(modal.getTopic())) topics.put(modal.getTopic(), new ArrayList<>());
            topics.get(modal.getTopic()).add(session);
            if (!sessions.containsKey(session)) sessions.put(session, new ArrayList<>());
            sessions.get(session).add(modal.getTopic());
        }
        else if (modal.getType() == MessageModal.MessageType.UNSUBSCRIBE) {
            if (!topics.containsKey(modal.getTopic())) return;
            topics.get(modal.getTopic()).remove(session);
        }
    }

    private Object[] resolveMethodArguments(Method method, MessageModal modal, WebSocketSession session) {
        List<Object> arguments = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] argTypes = method.getParameterTypes();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType() == SocketPayload.class) {
                    try {
                        arguments.add(objectMapper.readValue(modal.getPayload(), argTypes[i]));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                } else if (annotation.annotationType() == SocketPath.class) {
                    arguments.add(modal.getPath());
                    break;
                } else if (annotation.annotationType() == SocketTopic.class) {
                    arguments.add(modal.getTopic());
                    break;
                } else if (annotation.annotationType() == SocketAuthentication.class){
                    try {
                        arguments.add( session.getHandshakeInfo().getPrincipal());
                    }
                    catch (Exception e){
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
            if(arguments.size() != i+1) throw new RuntimeException("Argument has no annotation");
        }
        return arguments.toArray();
    }

    private <A extends Annotation> Mono<Pair<Object, Method>> findMethodByAnnotation(String path, Class<A> annotationType) {
        // Use reflection to find the method with the corresponding @MessageMapping annotation
        for (Object bean : applicationContext.getBeansWithAnnotation(SocketController.class).values()) {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
            if(!path.startsWith(targetClass.getAnnotation(SocketController.class).value())) break;

            for (Method method : targetClass.getMethods()) {
                A annotation = AnnotatedElementUtils.findMergedAnnotation(method, annotationType);
                if (annotation != null) {
                    try {
                        Method valueMethod = annotationType.getDeclaredMethod("value");
                        String value = (String) valueMethod.invoke(annotation);

                        if (path.endsWith(value)) {
                            return Mono.just(Pair.of(bean, method));
                        }
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                }
            }
        }
        return Mono.empty();
    }

    private void broadcast(String topic, Mono<?> message) {
        if (topics.containsKey(topic)) {
            message.subscribe(
                    m -> {
                        try {
                            String ans = objectMapper.writeValueAsString(m);
                            Flux.fromIterable(topics.get(topic))
                                    .flatMap(sess -> sess.send(Mono.just(sess.textMessage(ans))))
                                    .subscribe(); // Subscribe to the Flux
                        } catch (Exception e) {
                            log.error("Error during broadcasting: ", e);
                        }
                    },
                    error -> log.error("Error during broadcasting: ", error),
                    () -> log.debug("Broadcasting completed")
            );
        }
    }


}
