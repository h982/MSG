package com.web.c101.jwt;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@ToString
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String key; // uid가 들어간다.
    private String refreshToken; // Refresh Token String이 들어간다
    private String accessToken;


    public RefreshToken updateValue(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        return this;
    }

    @Builder
    public RefreshToken(String key, String refreshToken, String accessToken) {
        this.key = key;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
