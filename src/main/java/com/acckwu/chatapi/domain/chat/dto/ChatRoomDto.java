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
    private String lastMessageId;
}
