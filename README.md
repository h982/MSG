# SSAFY PJT II 맛집 소식 궁금해~!

## 목차

- [프로젝트 소개](#프로젝트-소개)
- [프로젝트 명세](#프로젝트-명세)
  - [개발 환경](#개발-환경)
  - [핵심 라이브러리](#핵심-라이브러리)
    <br>

## 프로젝트 소개

- 서비스명 : 맛소금
- 소재 : 맛집 정보 분석 서비스
- 맛집을 검색하면 해당 맛집의 여러 분석을 제공받는 시스템
  - 맛집의 평점을 알 수 있다.
  - 맛집의 평가가 긍정적인지 부정적인지 볼 수 있다.
  - 맛집의 리뷰들에서 많이 나온 단어들을 볼 수 있다.
  - 맛집의 비교분석을 볼 수 있다.
  <br>

## 프로젝트 명세

### 개발 환경

![image](/uploads/9a3a737334ccec85e09f063fe3b5570f/image.png)

## 기술 스택

### Back-End

<details>
    <summary>Back 자세히 살펴보기</summary>
    <ul>
      <li>기술스택 ⚙</li>
    </ul>
    <ul>
        <li>Spring-Boot : 2.5.4</li>
        <li>Spring-Boot-Data-JPA</li>
        <li>spring-boot-starter-security</li>
        <li>spring-boot-starter-jdbc</li>
        <li>swagger</li>
        <li>jjwt : 0.11.2</li>
        <li>lombok</li>
        <li>mysql : 8.0.22</li>
    </ul>
</details>


### Front-End / Android

- **지원 환경** : Web / Mobile 
<details>
    <summary>Front 자세히 살펴보기</summary>
    <ul>
        <li>기술스택 ⚙</li>
    </ul>   
    <ul>
        <li>JS, HTML, CSS</li>
        <li>TailWindCSS</li>
        <li>Vue.js</li>
    </ul>
    <li>--------------------------------------------------------------------------------------</li>
    <ul>
        <li>라이브러리 📚</li>
    </ul>   
    <ul>
        <li>axios</li>
        <li>eslint & prettier</li>
        <li>aos</li>
        <li>bootstrap</li>
        <li>bootstrap-vue</li>
        <li>jwt-decode</li>
        <li>vuetify</li>
        <li>vuetify-image-input</li>
        <li>vuex</li>
        <li>vuex-persistedstate</li
    </ul>
</details>



### Infra

  <details>
      <summary>개발, CI/CD 자세히 살펴보기</summary>
      <ul>
          <li>AWS EC2 - Deploy Server</li>
          <li>Docker
            <li>Mysql - DB Server</li>
            <li>Jenkins</li> 
          </li>
          <li>GitLab</li>
      	  <li>Hadoop</li>
      </ul>
  </details>




-------------------
### 핵심 라이브러리
- **카카오 로그인 API**

  - **링크** : https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api
  - **소개** : 카카오에서 제공하는 로그인 API
  - **사용 기능** : 카카오 로그인 API -> 소셜로그인 이용
  - **담당자** : 조성표
  
- **감성 분류 koBERT**
  - **링크** : https://github.com/SKTBrain/KoBERT
  - **소개** : 구글의 BERT 모델을 한국어로 사용가능하게 한 모델
  - **사용** **기능** : 맛집 리뷰로 전이학습시켜 리뷰 긍,부정 판단에 사용
  - **담당자** : 최광진
