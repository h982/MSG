package com.web.c101.config;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Stream;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 사용하는 Redis Client 팩토리 지정
        // Pool 사용 시 이곳에서 설정 가능
        RedisStandaloneConfiguration standaloneConfiguration =
                new RedisStandaloneConfiguration(host, port);
        standaloneConfiguration.setPassword(password.isEmpty() ? RedisPassword.none()
                : RedisPassword.of(password));

        return new LettuceConnectionFactory(standaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // Hash Operation 사용 시
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        // 혹은 아래 설정으로 모든 Key / Value Serialization을 변경할 수 있음
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean(name="sentimentDict")
    public Map<String, Integer> sentimentDict(){
        Map<String, Integer> map = new HashMap<>();
        ClassPathResource resource = new ClassPathResource("SentiWord_Dict.txt");

        try {
            InputStream inputStream = resource.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            while(input.ready()){
                StringTokenizer st = new StringTokenizer(input.readLine(), "\t");
                map.put(st.nextToken(), Integer.parseInt(st.nextToken()));
            }
        } catch (IOException e) {
            log.error("{}", e.getMessage(), e);
        }

        return map;
    }
}
