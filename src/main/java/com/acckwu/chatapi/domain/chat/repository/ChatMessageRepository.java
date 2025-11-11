package com.acckwu.chatapi.domain.chat.repository;

import com.acckwu.chatapi.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final DynamoDbEnhancedClient enhancedClient;

    // DynamoDB의 ChatMessages 테이블을 코드 객체로 매핑
    private DynamoDbTable<ChatMessage> table() {
        return enhancedClient.table("ChatMessages", TableSchema.fromBean(ChatMessage.class));
    }

    // ChatMessage 객체 저장
    public void save(ChatMessage msg) {
        table().putItem(msg);
    }

    // 해당 채팅방의 메시지 리스트를 시간 순으로 전부 가져옴
    public List<ChatMessage> findByRoomId(String chatRoomId) {
        return table().query(QueryConditional.keyEqualTo(
                Key.builder().partitionValue(chatRoomId).build())) // 특정 Partition Key(chatRoomId)에 해당하는 모든 아이템 조회
                .items().stream().toList();
    }
}
