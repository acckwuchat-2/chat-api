package com.acckwu.chatapi.domain.chat.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
}
