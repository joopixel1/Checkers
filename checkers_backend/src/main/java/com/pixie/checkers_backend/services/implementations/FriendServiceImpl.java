package com.pixie.checkers_backend.services.implementations;

import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.Chat;
import com.pixie.checkers_backend.models.entities.Friend;
import com.pixie.checkers_backend.models.entities.FriendRequest;
import com.pixie.checkers_backend.models.entities.User;
import com.pixie.checkers_backend.repositories.ChatRepository;
import com.pixie.checkers_backend.repositories.FriendRepository;
import com.pixie.checkers_backend.repositories.FriendRequestRepository;
import com.pixie.checkers_backend.repositories.UserRepository;
import com.pixie.checkers_backend.services.interfaces.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Override
    public Mono<Void> createFriend(String username, String friendId) {
        return friendRequestRepository.findById(friendId)
                .switchIfEmpty(Mono.error(new RuntimeException("FriendRequest not found for friendId: " + friendId)))
                .flatMap(FR -> {
                    if (FR.getInitializer().equals(username)) return Mono.error(new RuntimeException("Wait for other user to accept your request"));
                    Friend f = new Friend(null, new Friend.UserInfo(FR.getInitializer(), Boolean.FALSE, new Date()),
                            new Friend.UserInfo(FR.getAccepter(), Boolean.FALSE, new Date()), new Date());
                    return friendRequestRepository.deleteById(FR.getId())
                        .then(friendRepository.save(f).flatMap(nf -> Mono.when(
                                userRepository.findById(FR.getInitializer()).flatMap(u -> {
                                    u.getFriendIds().add(nf.getId());
                                    return userRepository.save(u);
                                }),
                                userRepository.findById(FR.getAccepter()).flatMap(u -> {
                                    u.getFriendIds().add(nf.getId());
                                    return userRepository.save(u);
                                }),
                                chatRepository.save(new Chat(
                                        null, Chat.ChatType.JOIN, new Date(),
                                        null, null, nf.getId()
                                ))
                        )));
        });
    }

    @Override
    public Mono<FriendRequest> createFriendRequest(String username, String friend){
        if (username.equals(friend)) return Mono.error(new RuntimeException("Username and friend must be different"));

        return friendRepository.existsByUser1IsAndUser2Is(username, friend)
                .or(friendRepository.existsByUser1IsAndUser2Is(friend, username))
                .flatMap(it -> {
                    if (it) return Mono.error(new RuntimeException("You guys are friends already"));

                    return friendRequestRepository.existsByInitializerIsAndAccepterIs(username, friend)
                            .or(friendRequestRepository.existsByInitializerIsAndAccepterIs(friend, username))
                            .flatMap(ex -> {
                                if (ex) return Mono.error(new RuntimeException("Friend Request Exists"));
                                FriendRequest FR = new FriendRequest(null, username, friend);
                                return friendRequestRepository.save(FR);
                            });
                });
    }

    @Override
    public Flux<FriendDTO> readFriends(String username) {
        return userRepository.findById(username).map(User::getFriendIds).flatMapMany(Flux::fromIterable)
                .flatMap(friendRepository::findById).map(it -> FriendDTO.mapToFriendDTO(it, username));
    }

    @Override
    public Flux<FriendRequest> readFriendRequestsPending(String username) {
        return friendRequestRepository.findAllByInitializerIs(username);
    }

    @Override
    public Flux<FriendRequest> readFriendRequestsWaiting(String username) {
        return friendRequestRepository.findAllByAccepterIs(username);
    }

    @Override
    public Mono<Void> deleteFriend(String friendId) {
        return friendRepository.deleteById(friendId)
                .then(chatRepository.save(new Chat(
                        null, Chat.ChatType.LEAVE, new Date(),
                        null, null, friendId
                )))
                .then(Mono.empty());
    }

    @Override
    public Flux<String> search(String username) {
        // TODO
        return null;
    }

}
