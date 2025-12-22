package com.acckwu.chatapi.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomDto {
    @JsonProperty("id")
    private String chatRoomId;
    private String name;
    @JsonProperty("lastMessage")
    private ChatMessageDto lastMessage;

    // 프론트(MyChatsPage)용 생성자
    public ChatRoomDto(String id, String name, ChatMessageDto lastMessage, Integer unreadCount) {
        this.chatRoomId = id;
        this.name = name;
        this.lastMessage = lastMessage;
    }

    // 기존 코드 호환용 생성자 (3번째 인자 String도 허용)
    public ChatRoomDto(String id, String name, String lastMessageId) {
        this.chatRoomId = id;
        this.name = name;
        this.lastMessage = null;  // 이 경로에서는 lastMessage 객체를 안 내려줌
    }
}
