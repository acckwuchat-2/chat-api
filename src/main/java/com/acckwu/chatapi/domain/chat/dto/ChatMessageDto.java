package com.acckwu.chatapi.domain.chat.dto;

import com.acckwu.chatapi.domain.user.dto.UserDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private String id;
    private String content;
    private String createdAt;
    private Long seq;
    private UserDto sender;
}
