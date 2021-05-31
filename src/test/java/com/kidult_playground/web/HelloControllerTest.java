package com.kidult_playground.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




/*
 ① @RunWith(SpringRunner.class)
   - 테스트 진행 시 JUnit에 내장된 실행자 외에 다른 실행자 실행
   - 이 테스트 클래스에서는 SpringRunner라는 스프링 실행자 사용
   - 즉 스프링 부트 테스트와 JUnit 사이에 연결자 역할.
 ② @WebMvcTest
   - 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션
   - 선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있음.
   - 단, @Service, @Component, @Repository 등은 사용할 수 없음. --> Real Test 용도의 어노테이션
   - 여기서는 컨트롤러만 사용하기 때문에 선언.
 ③ @Autowired
   - 스프링이 관리하는 빈(Bean)을 Injection --> bean = 객체(VO, dto, service 등등)
 ④ private MockMvc mvc;
   - 웹 API를 테스트할 때 사용.
   - 스프링 MVC 테스트의 시작점
   - 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트 가능.
 ⑤ mvc.perform(get("/hello"))
   - MockMvc를 통해 /hello 주소로 HTTP GET 요청
   - 체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언 가능.
 ⑥ .andExpect(status().isOk())
   - mvc.perform의 결과를 검증.
   - HTTP Header의 Status를 검증.
   - 우리가 흔히 알고 있는 200, 404, 500등의 상태 검증.
    -> 200(ok), 404(컨트롤러 매핑 에러, PageNotFound), 500(서버에러 ex. NullPointException)
   - 여기선 OK 즉, 200인지 아닌지 검증.
 ⑦ .andExpect(content().string(hello));
   - mvc.perform의 결과를 검증.
   - 응답 본문의 내용을 검증.
   - Controller에서 "hello"를 리턴하기 떄문에 이 값이 맞는지 검증.
 */
/*
 (1) param
   - API 테스트할 때 사용될 요청 파라미터를 설정.
   - 단, 값은 String만 허용
   - 그래서 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능.
 (2) jsonPath
   - JSON 응답값을 필드별로 검증할 수 있는 메소드
   - $를 기준으로 필드명 명시
   - 여기서는 name과 amount를 검증하니 $.name, $.amount로 검증.
 */
@RunWith(SpringRunner.class) // ①
@WebMvcTest(controllers = HelloController.class) // ②
public class HelloControllerTest {

    @Autowired // ③
    private MockMvc mvc; // ④

    @Test
    public void Return_hello() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello")) // ⑤
                .andExpect(status().isOk()) // ⑥
                .andExpect(content().string(hello)); // ⑦
    }

    @Test
    public void Return_helloDto() throws Exception {
        String name = "hello";  // 검증 대상 Dto와 동일 필드명 선언
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                    .param("name", name)    // (1)
                    .param("amount", String.valueOf(amount)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", is(name)))    // (2)
                        .andExpect(jsonPath("$.amount", is(amount)));
    }
}


