package com.web.c101;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class BackendApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        //SpringApplication.run(BackendApplication.class, args);

        SpringApplication app = new SpringApplication(BackendApplication.class);
        // pid 파일을 생성하는 writer 등록
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);

    }

}
