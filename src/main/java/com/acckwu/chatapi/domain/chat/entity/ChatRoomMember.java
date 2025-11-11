package com.acckwu.chatapi.domain.chat.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomMember {
    private String chatRoomId;
    private String userId;
    private String joinedAt;
    private String lastReadMessageId;

    @DynamoDbPartitionKey
    public String getChatRoomId() {
        return chatRoomId;
    }

    @DynamoDbSortKey
    public String getUserId() {
        return userId;
    }
}
