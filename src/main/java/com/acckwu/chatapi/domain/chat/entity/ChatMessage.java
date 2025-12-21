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
    @DynamoDbAttribute("chat_room_id")
    public String getChatRoomId() {
        return chatRoomId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @DynamoDbAttribute("sender_id")
    public String getSenderId() {
        return senderId;
    }

    @DynamoDbAttribute("message_id")
    public String getMessageId() {
        return messageId;
    }
}

