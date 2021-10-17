package com.web.c101.member;

import com.web.c101.jwt.TokenDto;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private boolean isValid;
    private String id;
    private Optional<TokenDto> optionalTokenDto;
}