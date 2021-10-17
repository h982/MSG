package com.web.c101.review;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    @Id
    private Long rid;

    private Long mid;

    // 리뷰 작성한 맛집과 리뷰 내용
    private String dong;
    private String store;
    private String content;

    // 별점
    private float star_score;

    // 리뷰 삭제 확인 변수
    private boolean flag;

    private LocalDateTime reg_date;

    MultipartFile[] multipartFiles;

    public boolean getFlag() {
        return this.flag;
    }

}
