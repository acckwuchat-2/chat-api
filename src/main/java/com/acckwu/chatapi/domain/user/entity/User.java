package com.acckwu.chatapi.domain.user.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String userId;
    private String username;
    private String email;
    private String password;

    // DynamoDB 테이블의 Partition Key(PK) 역할을 하는 속성 지정
    // Enhanced Client는 필드가 아니라 getter에 붙은 어노테이션을 기준으로 동작
    @DynamoDbPartitionKey
    @DynamoDbAttribute("user_id")
    public String getUserId() {
        return userId;
    }
}
