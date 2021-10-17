
package com.web.c101.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    /**
     * 실제 필터링 로직이 들어있다.
     * JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext에 저장하는 역할을 수행
     * 
     * Request Header에서 Access Token을 꺼내고 여러가지 검사 후 유저 정보를 꺼내서 SecurityContext에 저장
     * 가입 / 로그인 / 재발급을 제외한 모든 Request 요청은 이 필터를 거치기 때문에 토큰 정보가 없거나
     * 유효하지 않다면 정상적으로 수행되지 않는다.
     *
     * redis를 통해 해당 토큰이 키인 값이 있다면 이는 로그아웃한 키이므로 사용을 하지못하게했고
     * 또한, 로그아웃한 키가 유효기간이 끝나 사라져도 로그인한 키에도 없으므로
     * 이를 검사하게함
     * 
     * 요청이 정상적으로 Controller까지 도착했다면 SecurityContest에 Member id가 존재한다는 것이 보장
     * 
     * 대신 직접 DB를 조회한 것이 아니기 때문에 DB에 없는 경우와 같은 예외 상황은 Service 단에서 처리가 필요
     * 
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    @Transactional(readOnly = true)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Request Header에서 Access Token을 꺼낸다.
        String jwt = resolveToken(request);

        // validateToken으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext에 저장
        // 로그아웃 처리가 안된 토큰인지도 검사
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)
                && !redisTemplate.hasKey(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            String key = authentication.getName();

            // 로그인을 한 유저일 경우에만
            if (redisTemplate.hasKey(key)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                System.out.println("request cookie token : " + jwt);
            }
        }

        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        // 헤더에 넣는 방식
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
//            return bearerToken.substring(7);
//        }

        // 쿠키에서 토큰을 가져온다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
