package com.acckwu.chatapi.domain.chat.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    private String chatRoomId;
    private String name;
    private String lastMessageId;
    private String createdAt;

    @DynamoDbPartitionKey
    public String getChatRoomId() {
        return chatRoomId;
    }
}
