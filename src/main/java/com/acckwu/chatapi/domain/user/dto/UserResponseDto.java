package com.acckwu.chatapi.domain.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String userId;
    private String username;
    private String email;
}
