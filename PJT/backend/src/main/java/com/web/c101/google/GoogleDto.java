package com.web.c101.google;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleDto {
    // 리뷰 작성한 맛집과 리뷰 내용

    private String google_keyword;
    private String google_review_date;
    private int google_stars;
    private float google_star_avg;
    private String google_review_txt;
    private int google_emotion;
}
