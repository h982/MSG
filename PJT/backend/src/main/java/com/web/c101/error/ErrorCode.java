package com.web.c101.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음*/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 중복된 데이터 존재 */
    MEMBER_DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),

    NICKNAME_DUPLICATE_RESOUCE(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

    /**/
    MEMBER_FAIL(HttpStatus.CONFLICT, "회원가입 시 디비에서 오류 발생"),

    ERROR(HttpStatus.CONFLICT, "에러 발생"),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    KAKAO_LOGIN_EXCEPTION(HttpStatus.BAD_REQUEST, "카카오 "),

    MEMBER_LOGOUT(HttpStatus.BAD_REQUEST, "로그아웃된 사용자입니다."),

    VALIDATION_FAIL(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String detail;
}
