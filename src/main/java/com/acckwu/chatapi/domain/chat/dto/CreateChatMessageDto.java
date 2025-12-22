package com.acckwu.chatapi.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatMessageDto {
    private String chatRoomId;
    private String content;
}
