package com.acckwu.chatapi.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageDto {
    private String messageId;
    private String roomId;
    private String sender;
    private String timestamp;
}