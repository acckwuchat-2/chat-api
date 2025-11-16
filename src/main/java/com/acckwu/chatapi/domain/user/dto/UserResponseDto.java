package com.acckwu.chatapi.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
public class UserResponseDto {
    @JsonProperty("id")
    private String userId;
    private String username;
}
