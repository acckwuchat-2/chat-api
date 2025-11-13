package com.acckwu.chatapi.domain.user.service;

import com.acckwu.chatapi.domain.chat.dto.ChatRoomDto;
import com.acckwu.chatapi.domain.chat.entity.ChatRoomMember;
import com.acckwu.chatapi.domain.chat.repository.ChatRoomMemberRepository;
import com.acckwu.chatapi.domain.chat.repository.ChatRoomRepository;
import com.acckwu.chatapi.domain.user.entity.User;
import com.acckwu.chatapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ChatRoomMemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<ChatRoomDto> getUserChatRooms(String userId) {
        List<ChatRoomMember> memberships = memberRepository.findByUserId(userId);
        List<ChatRoomDto> result = new ArrayList<>();

        for (ChatRoomMember m : memberships) {
            chatRoomRepository.findById(m.getChatRoomId()).ifPresent(room -> {
                result.add(new ChatRoomDto(room.getChatRoomId(), room.getName(), room.getLastMessageId()));
            });
        }

        return result;
    }
}
