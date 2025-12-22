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
    public PageResponse<ChatRoomDto> getAllRooms(int page, int size) {
        List<ChatRoomDto> allRooms = chatRoomRepository.findAll();

        int totalElements = allRooms.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<ChatRoomDto> content =
                fromIndex >= totalElements ? List.of() : allRooms.subList(fromIndex, toIndex);

        PageInfo pageInfo = new PageInfo(
                totalPages,
                totalElements,
                size,
                page
        );

        return new PageResponse<>(content, pageInfo);
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

        return new ChatRoomDto(roomId, dto.getName(), (String) null);
    }

    // 특정 채팅방 조회
    public ChatRoomDto getChatRoom(String chatRoomId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        return new ChatRoomDto(room.getChatRoomId(), room.getName(), (String) null);
    }

    // 특정 채팅방의 메시지 목록 조회
    public PageResponse<ChatMessageDto> getMessages(String chatRoomId, int page, int size) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        List<ChatMessage> messages =
                chatMessageRepository.findByRoomIdOrdered(room.getChatRoomId());


        int totalElements = messages.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<ChatMessage> pageItems =
                fromIndex >= totalElements ? List.of() : messages.subList(fromIndex, toIndex);

        List<ChatMessageDto> content = new ArrayList<>();
        for (ChatMessage msg : pageItems) {
            User sender = userRepository.findById(msg.getSenderId()).orElse(null);

            content.add(ChatMessageDto.builder()
                    .id(msg.getMessageId())
                    .content(msg.getContent())
                    .createdAt(msg.getCreatedAt())
                    .seq(msg.getSeq())
                    .sender(sender != null ? new UserDto(sender.getUserId(), sender.getUsername()) : null)
                    .build());
        }

        PageInfo pageInfo = new PageInfo(
                totalPages,
                totalElements,
                size,
                page
        );

        return new PageResponse<>(content, pageInfo);
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

    public ChatMessageDto createAndSaveMessage(String roomId, String userId, String content) {
        String now = Instant.now().toString();

        // seq는 네 로직대로 (없으면 null or 증가값)
        ChatMessage saved = ChatMessage.builder()
                .chatRoomId(roomId)
                .senderId(userId)
                .messageId(UUID.randomUUID().toString())
                .content(content)
                .createdAt(now)
                .seq(null)
                .build();

        chatMessageRepository.save(saved);

        // sender 객체 만들기
        User sender = userRepository.findById(userId).orElse(null);
        UserDto senderDto = (sender != null)
                ? new UserDto(sender.getUserId(), sender.getUsername())
                : new UserDto("unknown", "알 수 없음");

        return ChatMessageDto.builder()
                .id(saved.getMessageId())
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt())
                .seq(saved.getSeq())
                .sender(senderDto)
                .build();
    }

    public void joinRoom(String roomId, String userId) {
        String now = Instant.now().toString();

        // 이미 가입한 경우 중복 저장 방지
        // (DynamoDB GSI user-index 사용)
        boolean alreadyJoined = memberRepository.findByUserId(userId).stream()
                .anyMatch(member -> roomId.equals(member.getChatRoomId()));

        if (alreadyJoined) {
            return;
        }

        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoomId(roomId)
                .userId(userId)
                .joinedAt(now)
                .build();

        memberRepository.save(member);
    }

    public void leaveRoom(String roomId, String userId) {
        memberRepository.deleteByChatRoomIdAndUserId(roomId, userId);
    }
}
