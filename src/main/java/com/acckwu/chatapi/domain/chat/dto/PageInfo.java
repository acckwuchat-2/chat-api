package com.acckwu.chatapi.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfo {
    private int totalPages;
    private int totalElements;
    private int size;
    private int number; // 현재 페이지 번호
}
