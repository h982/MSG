package com.web.c101.keyword;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class KeywordDto {
    private String word;
    private String count;

    public static List<KeywordDto> mapToList(Map<String, String> map) {
        List<KeywordDto> keywordDtos = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            keywordDtos.add(KeywordDto.builder()
                    .word(entry.getKey())
                    .count(entry.getValue())
                    .build()
            );
        }
        return keywordDtos;
    }

    public static Map<String, String> listToMap(List<KeywordDto> keywordDtos) {
        Map<String, String> keywordMap = new HashMap<>();
        for (KeywordDto keywordDto : keywordDtos) {
            keywordMap.put(keywordDto.getWord(), keywordDto.getCount());
        }
        return keywordMap;
    }
}
