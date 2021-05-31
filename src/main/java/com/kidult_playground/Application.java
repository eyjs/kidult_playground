package com.kidult_playground;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 메인클래스 : Application Class
 @SpringBootApplication 어노테이션 기능
  - 스프링 부트 자동 설정, 스프링 Bean Read&Create 자동 설정
  - @SpringBootApplication 어노테이션 기준으로 설정을 읽어감.
  - 항상 프로젝트 최상위 위치해야함.
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args){
        // 내장 WAS 실행
        SpringApplication.run(Application.class, args);
    }
}
