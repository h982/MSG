package com.web.c101.member.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SignUpRequest {
    String authorizeCode;
    String nickname;
}
