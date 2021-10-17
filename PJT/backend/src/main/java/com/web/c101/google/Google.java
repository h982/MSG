package com.web.c101.google;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@Builder
@Table(name = "google")
@NoArgsConstructor
@AllArgsConstructor
public class Google {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gid;
    @Column(name="google_keyword")
    private String googleKeyword;
    @Column(name="google_review_date")
    private String googleReviewDate;
    @Column(name="google_stars")
    private int googleStars;
    @Column(name="google_star_avg")
    private float googleStarAvg;
    @Column(name="google_review_txt")
    private String googleReviewTxt;
    @Column(name="google_emotion")
    private int googleEmotion;

}
