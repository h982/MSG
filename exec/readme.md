<!-- TABLE OF CONTENTS -->

<details open="open">
  <summary>포팅 메뉴얼</summary>
  <ol>
    <li>
        <a href="#built-with">Built With</a></li>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#Setting">Setting</a></li>
    <li><a href="#Additional Settings">Additional Settings</a></li>
    <li><a href="#Build & Deploy">Build & Deploy</a></li>
    <li><a href="#DB">DB</a></li>
  </ol>
</details>

## Built With

This section should list any major frameworks that you built your project using. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.
* [JDK-8](https://www.oracle.com/kr/java/technologies/javase/javase-jdk8-downloads.html)

    * Julu 1.8

* [Python](https://www.python.org/downloads/release/python-3810/)

    * 3.8.10

* [Spring Boot](https://spring.io/projects/spring-boot)

    * 2.5.4

* [Flask](https://flask.palletsprojects.com/en/2.0.x/)

    * 2.0.1

* [Vue.js](https://v3.vuejs-korea.org/)

    * vue/cli 4.5.13

* [Maven](https://maven.apache.org)

    * 3.6.3

* [JPA](https://spring.io/projects/spring-data-jpa)

* IDE
    * [InteliJ Ultimate 2021.2.2](https://blog.jetbrains.com/idea/2021/09/intellij-idea-2021-2-2/)
    * [Visual Studio Code 1.60.2](https://code.visualstudio.com/updates/v1_60)
    
* Web Server
    * [Nginx](https://www.nginx.com/)
    
* DB
    * [MySQL](https://www.mysql.com/)
    * [Redis](https://redis.io/)
    * [Elasticsearch](https://www.elastic.co/kr/?ultron=B-Stack-Trials-APJ-Exact&gambit=Stack-Core&blade=adwords-s&hulk=cpc&Device=c&thor=elasticsearch&gclid=EAIaIQobChMIksKFhpi38wIVx66WCh0TuQfvEAAYASAAEgLhlvD_BwE)
    
* Front

    * [Node.js](https://nodejs.org/download/release/v14.17.6/)

        * 14.17.6

    * [NPM](https://www.npmjs.com/)

        * 6.14.15

    * [YARN](https://yarnpkg.com/)

        * 1.22.11

        


<!-- GETTING STARTED -->
## Getting Started
### Prerequisites

```sh
$ sudo apt update && sudo apt -y upgrade

# Openjdk, python, Maven, npm, docker, nginx, redis 설치
$ sudo sudo apt install openjdk-8-jdk && sudo apt get install python3 && sudo apt install maven && sudo apt install npm && sudo apt install docker && sudo apt install nginx && sudo install redis
```

- ##### [카카오 로그인 api 키 발급](https://developers.kakao.com/)

- ##### Docker Mysql 설치 및 실행

```sh
$ sudo service docker start
$ docker pull mysql
$ mkdir mysqldata

# 도커에서 내부 네트워크를 사용하기 위한 설정 + 컨테이너 생성 
$ sudo docker run -d -p 3306:3306 --name mysql --network innerconn -e MYSQL_ROOT_PASSWORD=white123 -v ~/home/ubuntu/docker/mysql-data/:/var/lib/mysql mysql --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

# 다른 도커 설치들 써줄 것
```



### Installation

1. Project URL
    * [맛소금](https://lab.ssafy.com/s05-bigdata-dist/S05P21C101)
2. Clone the repo
   ```sh
   git clone https://lab.ssafy.com/s05-bigdata-dist/S05P21C101.git
   ```
   



### Setting

1. DB 설정
    ```sh
    $ sudo docker exec -it mysql /bin/bash
    
    MariaDB > create user 'white'@'%' IDENTIFIED BY 'trend123';
    grant all privileges on *.* to 'white'@'%';
    flush privileges;
    
    MariaDB > create database trend;
    MariaDB > use trend;
    
    # 덤프파일을 사용하여 db 세팅 필요
    ```
2. Nginx 설정
    ```sh
    $ sudo vim /etc/nginx/sites-enabled/default
    
    	upstream backend {
            server localhost:8080;
            server localhost:8081;
    	}
    	server {
    		# 젠킨스 실행 위치
            root /home/ubuntu/deploy/dist;
            autoindex_localtime on;
            
            # Add index.php to the list if you are using PHP
            index index.html index.htm index.nginx-debian.html;
    
            #server_name _;
            server_name j5c101.p.ssafy.io;
            charset utf-8;
    
            location / {
                    try_files $uri $uri/ /index.html;
            }
    
            ### backend reverse proxy 설정 추가 ###
            location /api {
                    # upstream으로 변경
                    #proxy_pass http://localhost:8080;
                    proxy_pass http://backend;
                    proxy_http_version 1.1;
                    proxy_set_header Connection "";
    
                    proxy_set_header Host $host;
                    proxy_set_header X-Real-IP $remote_addr;
                    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                    proxy_set_header X-Forwarded-Proto $scheme;
                    proxy_set_header X-Forwarded-Host $host;
                    proxy_set_header X-Forwarded-Port $server_port;
            }
            ### 여기까지 ###
            
            ### 아래 내용은 Certbot을 통해 자동 생성 ###
            listen 443 ssl; # managed by Certbot
            ssl_certificate /etc/letsencrypt/live/j5c101.p.ssafy.io/fullchain.pem; # managed by Certbot
            ssl_certificate_key /etc/letsencrypt/live/j5c101.p.ssafy.io/privkey.pem; # managed by Certbot
            include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
            ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
        }
    ```
    ```sh
    #설정 적용
    $ sudo nginx –t
    $ sudo service nginx restart
    ```

3. Certbot 설정

   ```sh
   # Certbot 저장소 설정 및 설치
   $ sudo apt-get update
   $ sudo apt-get install software-properties-common
   $ sudo add-apt-repository universe
   $ sudo add-apt-repository ppa:certbot/certbot
   $ sudo apt-get update
   $ sudo apt-get install certbot python3-certbot-nginx
   ```

   ```sh
   # SSL 인증서 획득, 이때 주소는 nginx설정의 server_name을 작성
   $ sudo certbot --nginx -d j5c101.p.ssafy.io
   ```

   ```sh
   # 인증서 자동 갱신
   $ sudo certbot renew --dry-run
   ```

   

## Additional Settings

### Elasticsearch

- #### 설치

참고자료 :  https://velog.io/@qnfmtm666/elasticsearch-Elasticsearch-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0-Ubuntu-20.04



- #### 외부 접속 & 보안


1. 설정파일인 **elasticsearch.yml** 파일 접근하기

```
sudo vim /etc/elasticsearch/elasticsearch.yml
```



2. 외부 접속 허용

network.host를 0.0.0.0로 변경하고 cluster.initial_master_nodes의 주석을 풀어준다.
이때 ' : ' 다음에 반드시 띄어쓰기를 해야한다.



3. 보안 추가

elasticsearch의 최소한의 보안설정을 위해 다음을 추가

```
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
```

elasticsearch를 실행하고 'bin/elasticsearch-setup-passwords interactive' 를 EC2에 입력하고 비밀번호를 설정한다

- 맛소금 프로젝트 ES 비밀번호 : msg!234



- #### EC2에서 실행
  - 실행 : sudo service elasticsearch start
  - 실행취소 : sudo service elasticsearch stop

  

------



### Redis


```sh
# 1.apt-get 업데이트 & make & gcc 설치

$ apt-get update
$ apt-get install gcc
$ apt-get install make

# 2. Redis 설치 (다운로드, 압축풀기, 빌드)

$ wget https://download.redis.io/releases/redis-6.2.5.tar.gz
$ tar xzf redis-6.2.5.tar.gz
$ cd redis-6.2.5
$ make

#3. Redis 실행 & CLI 접속
$ src/redis-server
$ src/redis-cli
```



------



### Flask

```sh
# dockerfile 위치로 이동
$ cd /SUB3/flask/app/
   
# docker 컨테이너 빌드
$ docker build --tag flask_test_1 .
   
# docker 에서 google-chrome 설치 (Dockerfile) 
$ docker exec -it flask_t /bin/bash
   
$ wget -q -O-https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add-
   
$ sudo sh -c 'echo "deb [arch = amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list '
   
$ sudo apt update
$ sudo apt install google-chrome-stable
```



## Build & Deploy

### Frontend

```sh
### frontend build ###
$ cd PJT
$ cd frontend
$ npm install -g yarn
$ yarn install
$ npx tailwindcss-cli@latest build -o tailwind.css
$ mv tailwind.css public/style.css
# VUE_APP_KAKAO_ID에 발급받은 카카오 로그인 api 키를 입력
$ echo -e "VUE_APP_KAKAO_ID=카카오키를입력\nVUE_APP_LOGIN_KAKAO_URI=https://j5c101.p.ssafy.io/login \nVUE_APP_SIGNUP_KAKAO_URI=https://j5c101.p.ssafy.io/signup \nVUE_APP_LOGOUT_KAKAO_URI=https://j5c101.p.ssafy.io/main \nVUE_APP_GOOGLE_MAP_API_KEY=AIzaSyByaqJv-Pm0IMDUZkMzOyFZ80BTR4Ti7og" >> .env.local
$ find ./ -name "*.js" -exec sed -i "s/http:\/\/localhost:8080/https:\/\/j5c101.p.ssafy.io\/api/g" {} \;
$ yarn run build
```

### Backend

```sh
$ cd PJT
$ cd backend
$ sudo mvn package
```



## DB

* 계정: white
* schema : trend123
* properties
    * application.properties



## CI/CD

* Jenkins
* Setting

    - 배포용 폴더 생성
      ```sh
      $ mkdir deploy
      ```

    - 빌드용 properties 파일 생성

      ```sh
      $ vi deployproperties.properties
      ```

      ```properties
      spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
      spring.datasource.url=jdbc:mysql://localhost:3306/trend?useUniCode=yes&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
      spring.datasource.username=white
      spring.datasource.password=trend123
      
      spring.redis.host=localhost
      spring.redis.port=6379
      spring.redis.password=ssafy
      
      jwt.secret=c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK
      
      kakao.key=발급받은카카오로그인api키
      
      logging.level.root=info
      logging.level.sql=info
      logging.level.web=info
      ```

    - restart shell

      ```sh
      kill $(cat deploy/app.pid)
      
      nohup java -jar deploy/*.jar --server.servlet.context-path=/api --server.address=127.0.0.1 --server.port=8080 --spring.config.location=/home/ubuntu/deploy/deployproperties.properties --spring.pid.file=deploy/app.pid >> deploy/app.log 2>&1 
      ```

