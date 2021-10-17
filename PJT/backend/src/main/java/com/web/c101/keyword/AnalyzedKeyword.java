package com.web.c101.keyword;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.ToString;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AnalyzedKeyword {
    private String keyword;
    private int count;
    private String sentiment;

    public static AnalyzedKeyword keywordToAnalyzed(KeywordDto keywordDto, String sentiment) {
        return AnalyzedKeyword.builder()
                .keyword(keywordDto.getWord())
                .count(Integer.parseInt(keywordDto.getCount()))
                .sentiment(sentiment)
                .build();
    }
}