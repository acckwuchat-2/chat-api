package com.acckwu.chatapi.domain.user.controller;

import com.acckwu.chatapi.domain.auth.CustomUserDetails;
import com.acckwu.chatapi.domain.chat.dto.ChatRoomDto;
import com.acckwu.chatapi.domain.user.dto.UserResponseDto;
import com.acckwu.chatapi.domain.user.entity.User;
import com.acckwu.chatapi.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 내 정보 조회 API
    @GetMapping("/me")
    public UserResponseDto getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserById(userDetails.getUserId());

        return new UserResponseDto(user.getUserId(), user.getUsername());
    }

    // 참여 중인 채팅방 목록 조회 API
    @GetMapping("/chat-rooms")
    public List<ChatRoomDto> getMyChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUserChatRooms(userDetails.getUserId());
    }
}
