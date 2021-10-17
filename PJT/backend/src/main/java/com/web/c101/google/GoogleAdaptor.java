package com.web.c101.google;

public class GoogleAdaptor {
    public static GoogleDto entityToDto(Google google) {
        return GoogleDto.builder()
                .google_keyword(google.getGoogleKeyword())
                .google_review_date(google.getGoogleReviewDate())
                .google_stars(google.getGoogleStars())
                .google_star_avg(google.getGoogleStarAvg())
                .google_review_txt(google.getGoogleReviewTxt())
                .google_emotion(google.getGoogleEmotion())
                .build();
    }
}
