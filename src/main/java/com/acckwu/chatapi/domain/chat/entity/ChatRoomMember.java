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
    @DynamoDbSecondarySortKey(indexNames = "user-index")
    @DynamoDbAttribute("chat_room_id")
    public String getChatRoomId() {
        return chatRoomId;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "user-index")
    @DynamoDbAttribute("user_id")
    public String getUserId() {
        return userId;
    }

    @DynamoDbAttribute("joined_at")
    public String getJoinedAt() {
        return joinedAt;
    }

    @DynamoDbAttribute("last_read_message_id")
    public String getLastReadMessageId() {
        return lastReadMessageId;
    }
}
