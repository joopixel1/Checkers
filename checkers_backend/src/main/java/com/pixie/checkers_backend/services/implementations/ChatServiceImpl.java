package com.pixie.checkers_backend.services.implementations;

import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.Chat;
import com.pixie.checkers_backend.models.modals.ChatModal;
import com.pixie.checkers_backend.models.modals.ChatUpdateModal;
import com.pixie.checkers_backend.repositories.ChatRepository;
import com.pixie.checkers_backend.repositories.FriendRepository;
import com.pixie.checkers_backend.services.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final FriendRepository friendRepository;


    @Override
    public Mono<Chat> createChat(String username, ChatModal modal) {
        return chatRepository.save(new Chat(
                    null, Chat.ChatType.CHAT, new Date(),
                    modal.getContent(), username, modal.getFriendID()
            ));
    }

    @Override
    public Flux<Chat> readChat(String friendID, Integer page) {
        return chatRepository.findAllByFriendIDIsOrderByTimeStampAsc(friendID, PageRequest.of(page, 50));
    }

    @Override
    public Mono<Chat> updateChat(String username, ChatUpdateModal modal) {
        return chatRepository.findById(modal.getChatID())
                .switchIfEmpty(Mono.error(new RuntimeException("No chat found for chatId: " + modal.getChatID())))
                .flatMap(c -> {
                    if(!c.getSender().equals(username)) return Mono.error(new RuntimeException("You are not the sender"));
                    c.setContent(modal.getContent());
                    c.setTimeStamp(new Date());
                    return chatRepository.save(c);
                });
    }

    @Override
    public Mono<Chat> deleteChat(String username, String chatID) {
        return chatRepository.findById(chatID)
                .switchIfEmpty(Mono.error(new RuntimeException("No chat found for chatId: " + chatID)))
                .flatMap(c -> {
                    if(!c.getSender().equals(username)) return Mono.error(new RuntimeException("You are not the sender"));
                    c.setTimeStamp(new Date());
                    c.setType(Chat.ChatType.DELETED);
                    c.setContent("");
                    return chatRepository.save(c);
                });
    }

    @Override
    public Mono<FriendDTO> subscribeChat(String username, String friendID) {
        return friendRepository.findById(friendID).flatMap(friend -> {
            if(friend.getUser1().equals(username)) friend.getUserInfo1().setOnline(Boolean.TRUE);
            else friend.getUserInfo2().setOnline(Boolean.TRUE);
            return friendRepository.save(friend);
        }).map(friend -> FriendDTO.mapToFriendDTO(friend, username));
    }

    @Override
    public Mono<FriendDTO> unsubscribeChat(String username, String friendID) {
        return friendRepository.findById(friendID).flatMap(friend -> {
            if(friend.getUser1().equals(username)) friend.getUserInfo1().setLastOnline(new Date());
            else friend.getUserInfo2().setLastOnline(new Date());
            return friendRepository.save(friend);
        }).map(friend -> FriendDTO.mapToFriendDTO(friend, username));
    }

}
