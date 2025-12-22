package com.acckwu.chatapi.domain.user.service;

import com.acckwu.chatapi.domain.chat.dto.ChatMessageDto;
import com.acckwu.chatapi.domain.chat.dto.ChatRoomDto;
import com.acckwu.chatapi.domain.chat.entity.ChatMessage;
import com.acckwu.chatapi.domain.chat.entity.ChatRoomMember;
import com.acckwu.chatapi.domain.chat.repository.ChatMessageRepository;
import com.acckwu.chatapi.domain.chat.repository.ChatRoomMemberRepository;
import com.acckwu.chatapi.domain.chat.repository.ChatRoomRepository;
import com.acckwu.chatapi.domain.user.dto.UserDto;
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
    private final ChatMessageRepository chatMessageRepository;

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<ChatRoomDto> getUserChatRooms(String userId) {
        List<ChatRoomMember> memberships = memberRepository.findByUserId(userId);
        List<ChatRoomDto> result = new ArrayList<>();

        for (ChatRoomMember m : memberships) {
            chatRoomRepository.findById(m.getChatRoomId()).ifPresent(room -> {

                // lastMessageId로 찾지 말고, roomId 최신 메시지 1개로 만든다
                ChatMessageDto lastMessageDto = null;

                ChatMessage last = chatMessageRepository
                        .findLatestByRoomId(room.getChatRoomId())
                        .orElse(null);

                if (last != null) {
                    User sender = userRepository.findById(last.getSenderId()).orElse(null);

                    // 프론트가 sender.username을 무조건 읽으니 null 금지
                    UserDto senderDto = (sender != null)
                            ? new UserDto(sender.getUserId(), sender.getUsername())
                            : new UserDto("unknown", "알 수 없음");

                    lastMessageDto = ChatMessageDto.builder()
                            .id(last.getMessageId())
                            .content(last.getContent())
                            .createdAt(last.getCreatedAt())
                            .seq(last.getSeq())
                            .sender(senderDto)
                            .build();
                }

                result.add(new ChatRoomDto(
                        room.getChatRoomId(),
                        room.getName(),
                        lastMessageDto,
                        0 // unreadCount는 일단 0
                ));
            });
        }

        return result;
    }
}
