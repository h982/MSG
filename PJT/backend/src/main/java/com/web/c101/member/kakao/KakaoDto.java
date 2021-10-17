package com.web.c101.member.kakao;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KakaoDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String refresh_token_expires_in;
}
