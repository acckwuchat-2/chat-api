package com.acckwu.chatapi.domain.chat.controller;

import com.acckwu.chatapi.domain.auth.CustomUserDetails;
import com.acckwu.chatapi.domain.chat.dto.*;
import com.acckwu.chatapi.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 모든 채팅방 조회
    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getChatRooms() {
        return ResponseEntity.ok(chatService.getAllRooms());
    }

    // 새 채팅방 생성
    @PostMapping
    public ResponseEntity<ChatRoomDto> createRoom(@RequestBody CreateChatRoomDto dto,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(chatService.createRoom(dto, user.getUserId()));
    }

    // 특정 채팅방 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDto> getChatRoom(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(chatService.getChatRoom(chatRoomId));
    }

    // 특정 채팅방의 메시지 목록 조회
    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(chatService.getMessages(chatRoomId));
    }

    // 마지막 메시지 조회
    @GetMapping("/{chatRoomId}/last-message")
    public ResponseEntity<ChatMessageDto> getLastMessage(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(chatService.getLastMessage(chatRoomId));
    }
}
