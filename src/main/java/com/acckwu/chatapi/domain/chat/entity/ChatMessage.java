package com.acckwu.chatapi.domain.chat.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private String chatRoomId;
    private String senderId;
    private String messageId;
    private String content;
    private String createdAt;
    private Long seq;

    @DynamoDbPartitionKey
    public String getChatRoomId() {
        return chatRoomId;
    }

    @DynamoDbSortKey
    public String getCreatedAt() {
        return createdAt;
    }
}
