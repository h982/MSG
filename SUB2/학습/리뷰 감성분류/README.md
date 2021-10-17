# 리뷰 감성분류

### 참고 문서

https://dacon.io/codeshare/3062

https://tech-diary.tistory.com/31

### 자료

- mac_train.csv : 맥도날드 트위터
- soon_train.csv : 할매순대국 트위터
- ratings_train.txt : 라벨링된 영화리뷰

### 코드

- naver_review_classifications_pytorch_kobert.ipynb : 영화리뷰 예제
- 작성중.ipynb : 맛집리뷰 작성중 예제 



## 전이학습

koBERT

다이닝코드의 리뷰가 존재하는 식당들의 평점을 기준으로 전이학습을 진행하였다.

별점 1~2점 부정

별점 3점 중립

별점 4~5점 긍정



### 자료

- 210916_train.txt : 학습용 리뷰데이터
- 210916_test.txt : 테스트용 리뷰데이터(미완성)
- koBERT.ipynb : 코드

