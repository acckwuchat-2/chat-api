package com.acckwu.chatapi.domain.chat.controller;

import com.acckwu.chatapi.domain.chat.dto.CreateChatMessageDto;
import com.acckwu.chatapi.domain.chat.dto.LeaveChatRoomDto;
import com.acckwu.chatapi.domain.chat.dto.MessageDto;
import com.acckwu.chatapi.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/send")
    public void sendChatMessage(CreateChatMessageDto request,
                                @Header(name = "Authorization", required = false) String authorization) {

        String userId = extractUserIdFromAuthorization(authorization);

        // Long → String 변환 (도메인은 String chatRoomId)
        String roomIdStr = String.valueOf(request.getChatRoomId());

        MessageDto messageDto =
                chatService.createAndSaveMessage(roomIdStr, userId, request.getContent());

        // springwolf 명세: sub/chat_messages 로 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat_messages", messageDto);
    }

    @MessageMapping("/chat/leave")
    public void leaveChatRoom(LeaveChatRoomDto request,
                              @Header(name = "Authorization", required = false) String authorization) {

        String userId = extractUserIdFromAuthorization(authorization);

        // Long → String (내부 도메인 chatRoomId는 String이기 때문)
        String roomIdStr = String.valueOf(request.getRoomId());

        chatService.leaveRoom(roomIdStr, userId);
    }

    private String extractUserIdFromAuthorization(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return "anonymous";
        }
        // TODO: auth 도메인(JWT) 연동해서 실제 userId 추출
        return "user-id-from-token";
    }
}
