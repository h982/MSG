package com.web.c101.review;

import java.time.LocalDateTime;

public class ReviewAdaptor {
    public static ReviewDto entityToDto(Review review) {
        return ReviewDto.builder()
                .rid(review.getRid())
                .mid(review.getMid())
                .dong(review.getDong())
                .store(review.getStore())
                .content(review.getContent())
                .star_score(review.getStarScore())
                .reg_date(review.getRegDate())
                .flag(review.getFlag())
                .build();
    }

    public static Review dtoToEntity(ReviewDto reviewDto) {
        return Review.builder()
                .mid(reviewDto.getMid())
                .dong(reviewDto.getDong())
                .store(reviewDto.getStore())
                .content(reviewDto.getContent())
                .starScore(reviewDto.getStar_score())
                .regDate(LocalDateTime.now())
                .flag(reviewDto.getFlag())
                .build();
    }
}
