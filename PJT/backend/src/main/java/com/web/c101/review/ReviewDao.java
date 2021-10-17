package com.web.c101.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewDao extends JpaRepository<Review, Long> {

    Optional<Review> findReviewByRid(Long rid);

    List<Review> findReviewByMid(Long mid);

    List<Review> findReviewByStore(String store);

    List<Review> findReviewByDongAndStore(String dong, String store);

    Slice<Review> findReviewByMidAndFlagIsTrue(Long mid, Pageable pageable);

    long countReviewByMidAndFlagIsTrue(Long mid);

}
