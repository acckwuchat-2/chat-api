package com.acckwu.chatapi.domain.chat.service;

import com.acckwu.chatapi.domain.chat.dto.*;
import com.acckwu.chatapi.domain.chat.entity.*;
import com.acckwu.chatapi.domain.chat.repository.*;
import com.acckwu.chatapi.domain.user.dto.UserDto;
import com.acckwu.chatapi.domain.user.entity.User;
import com.acckwu.chatapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository memberRepository;
    private final UserRepository userRepository;

    // 모든 채팅방 조회
    public List<ChatRoomDto> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    // 새 채팅방 생성
    public ChatRoomDto createRoom(CreateChatRoomDto dto, String creatorId) {
        String roomId = UUID.randomUUID().toString();
        String now = Instant.now().toString();

        ChatRoom room = ChatRoom.builder()
                .chatRoomId(roomId)
                .name(dto.getName())
                .lastMessageId(null)
                .createdAt(now)
                .build();

        chatRoomRepository.save(room);

        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoomId(roomId)
                .userId(creatorId)
                .joinedAt(now)
                .build();

        memberRepository.save(member);

        return new ChatRoomDto(roomId, dto.getName(), null);
    }

    // 특정 채팅방 조회
    public ChatRoomDto getChatRoom(String chatRoomId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        return new ChatRoomDto(room.getChatRoomId(), room.getName(), room.getLastMessageId());
    }

    // 특정 채팅방의 메시지 목록 조회
    public List<ChatMessageDto> getMessages(String chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByRoomId(chatRoomId);
        List<ChatMessageDto> result = new ArrayList<>();

        for (ChatMessage msg : messages) {
            User sender = userRepository.findById(msg.getSenderId()).orElse(null);

            result.add(ChatMessageDto.builder()
                    .id(msg.getMessageId())
                    .content(msg.getContent())
                    .createdAt(msg.getCreatedAt())
                    .seq(msg.getSeq())
                    .sender(sender != null ? new UserDto(sender.getUserId(), sender.getUsername()) : null)
                    .build());
        }

        return result;
    }

    // 마지막 메시지 조회
    public ChatMessageDto getLastMessage(String chatRoomId) {
        ChatMessage msg = chatMessageRepository.findLastMessage(chatRoomId);

        if (msg == null) {
            return null;
        }

        User sender = userRepository.findById(msg.getSenderId()).orElse(null);

        return ChatMessageDto.builder()
                .id(msg.getMessageId())
                .content(msg.getContent())
                .createdAt(msg.getCreatedAt())
                .seq(msg.getSeq())
                .sender(sender != null ? new UserDto(sender.getUserId(), sender.getUsername()) : null)
                .build();
    }
}
