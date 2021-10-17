package com.web.c101.config;

import com.web.c101.config.security.JwtAccessDeniedHandler;
import com.web.c101.config.security.JwtAuthenticationEntryPoint;
import com.web.c101.config.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security의 가장 기본적인 설정
 * 
 * Component 어노테이션을 설정해야 스프링이 스캔하고 객체 주입이 가능하다
 */
@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final RedisTemplate redisTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/favicon.ico", "/v2/api-docs",
                        "/configuration/**", "/swagger*/**",
                        "/webjars/**", "/swagger/**", "/css/**", "/fonts/**", "/assets/**", "/img/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions()
                .sameOrigin()
                .disable();

        // CSRF 설정 Disable
        http.cors().configurationSource(corsConfigurationSource()).and()
                .csrf().disable()
                // exception handling을 하기 위해 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 시큐리티는 기본적으로 세션을 사용
                // 토큰 기반 인증이므로 세션을 사용하지 않기 때문에
                // 세션설정을 stateless로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()// httpServletRequest를 사용하는 접근을 제한하겠다는 의미
//                .antMatchers("/member/**", "/search/**", "/realtime").permitAll() // 해당 주소로 시작되면 접근을 제한하지 않는다.
//                .anyRequest().authenticated() // /auth를 통해 들어오는 주소가 아니면 전부 토큰 인증이 필요
                .antMatchers("/review/addReview", "/review/deleteReview", "/review/userReviewList").authenticated()
                .anyRequest().permitAll()

                // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스를 등록
                .and()
                .apply(new JwtSecurityConfig(tokenProvider, redisTemplate));
    }

    /**
     * CORS 허용
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
