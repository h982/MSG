package com.web.c101;

import com.web.c101.keyword.AnalyzedKeyword;
import com.web.c101.keyword.KeywordDto;
import com.web.c101.keyword.KeywordService;
import com.web.c101.keyword.KeywordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@SpringBootTest
class RedisRepositoryTest {
    @Autowired
    private KeywordService keywordService;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "sentimentDict")
    private Map<String, Integer> sentimentDict;

    @Test
    void testSentimentDict() {
        Assertions.assertTrue(sentimentDict.isEmpty(), "SentimentDict load fail");
    }

    @Test
    void testKeywordRepository() {
        List<KeywordDto> keywords = keywordRepository.getKeywords("test");
        Assertions.assertTrue(keywords.size() == 0, "get keyword from Redis fail");

        keywords.add(KeywordDto.builder().word("test").count("0").build());
        keywordRepository.replaceKeywords("test", keywords);

        List<KeywordDto> newKeywords = keywordRepository.getKeywords("test");
        Assertions.assertTrue(newKeywords.size() != 21, "Redis replace Error");
    }

    @Test
    void testKeywordService() {
        redisTemplate.opsForHash().put("test", "test", "0");
        List<AnalyzedKeyword> keywords = keywordService.getKeywords("test");

        Assertions.assertTrue(keywords.size() != 20, "Reduced Fail");
    }

}
