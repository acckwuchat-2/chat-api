package com.acckwu.chatapi.domain.chat.repository;

import com.acckwu.chatapi.domain.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final DynamoDbEnhancedClient enhancedClient;

    // DynamoDB의 ChatRooms 테이블을 코드 객체로 매핑
    private DynamoDbTable<ChatRoom> table() {
        return enhancedClient.table("ChatRooms", TableSchema.fromBean(ChatRoom.class));
    }

    // ChatRoom 객체 저장
    public void save(ChatRoom room) {
        table().putItem(room);
    }

    // chat_room_id가 일치하는 채팅방 조회
    public Optional<ChatRoom> findById(String id) {
        return Optional.ofNullable(
                table().getItem(Key.builder().partitionValue(id).build())
        );
    }
}
