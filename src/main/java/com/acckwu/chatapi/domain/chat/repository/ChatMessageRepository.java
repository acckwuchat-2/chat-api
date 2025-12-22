package com.acckwu.chatapi.domain.chat.repository;

import com.acckwu.chatapi.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<ChatMessage> findByRoomIdOrdered(String chatRoomId) {
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder().partitionValue(chatRoomId).build()
                        )
                )
                .scanIndexForward(false) // 시간 오름차순
                .build();

        List<ChatMessage> result = new ArrayList<>();
        table().query(request).items().forEach(result::add);
        return result;
    }


    // 해당 채팅방의 마지막 메시지를 가져옴
    public ChatMessage findLastMessage(String chatRoomId) {
        return table().query(r -> r
                        .queryConditional(QueryConditional.keyEqualTo(
                                Key.builder().partitionValue(chatRoomId).build()))
                        .scanIndexForward(false) // 내림차순
                        .limit(1))
                .items()
                .stream()
                .findFirst()
                .orElse(null);
    }

    // roomId 기준 "가장 최근 메시지 1개" 조회
    public Optional<ChatMessage> findLatestByRoomId(String chatRoomId) {
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(
                        Key.builder().partitionValue(chatRoomId).build()
                ))
                .scanIndexForward(false) // 최신부터 (sort key desc)
                .limit(1)
                .build();

        PageIterable<ChatMessage> pages = table().query(request);

        return pages.items().stream().findFirst();
    }

    //메시지 단건 조회
    public Optional<ChatMessage> findById(String messageId) {
        return Optional.ofNullable(
                table().getItem(
                        Key.builder()
                                .partitionValue(messageId)
                                .build()
                )
        );
    }
}
