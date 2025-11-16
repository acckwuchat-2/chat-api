package com.acckwu.chatapi.domain.user.repository;

import com.acckwu.chatapi.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final DynamoDbEnhancedClient enhancedClient;

    // DynamoDB의 Users 테이블을 코드 객체로 매핑
    private DynamoDbTable<User> table() {
        return enhancedClient.table("Users", TableSchema.fromBean(User.class));
    }

    // User 객체 저장
    public void save(User user) {
        table().putItem(user);
    }

    // userId를 PK로 하는 User 조회
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(
                table().getItem(Key.builder().partitionValue(userId).build())
        );
    }

    // 테이블 전체를 읽어온 후 username이 일치하는 첫 번째 항목 조회
    public Optional<User> findByUsername(String username) {
        return table().scan().items().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}
