package com.acckwu.chatapi.domain.chat.repository;

import com.acckwu.chatapi.domain.chat.entity.ChatRoomMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomMemberRepository {
    private final DynamoDbEnhancedClient enhancedClient;

    // DynamoDB의 ChatRoomMembers 테이블을 코드 객체로 매핑
    private DynamoDbTable<ChatRoomMember> table() {
        return enhancedClient.table("ChatRoomMembers", TableSchema.fromBean(ChatRoomMember.class));
    }

    // ChatRoomMember 객체 저장
    public void save(ChatRoomMember member) {
        table().putItem(member);
    }

    // chat_room_id가 일치하는 채팅방의 모든 멤버 조회
    public List<ChatRoomMember> findByChatRoom(String chatRoomId) {
        return table().query(QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(chatRoomId)
                .build()))
                .items().stream().toList();
    }

    // GSI로 user_id(PK) -> chat_room_id(SK) 매핑 조회, 즉 특정 유저가 어떤 채팅방에 속했는지 빠르게 조회
    public List<ChatRoomMember> findByUserId(String userId) {
        DynamoDbIndex<ChatRoomMember> index = table().index("user-index");

        return index.query(QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(userId)
                .build()))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
}
