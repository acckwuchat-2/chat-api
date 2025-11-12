package com.acckwu.chatapi.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomDto {
    private String chatRoomId;
    private String name;
    private String lastMessageId;
}
