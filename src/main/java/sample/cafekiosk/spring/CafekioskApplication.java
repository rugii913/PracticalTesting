package sample.cafekiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 바로 Spring을 사용한 app을 만들 것은 아니라서 일단 안 쓰도록 spring 패키지 만들어서 옮겨 두었음
@SpringBootApplication
public class CafekioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafekioskApplication.class, args);
    }

}
