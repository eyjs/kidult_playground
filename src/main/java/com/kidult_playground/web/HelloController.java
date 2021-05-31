package com.kidult_playground.web;
import com.kidult_playground.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 @RestController 어노테이션
  - 컨트롤러를 JSON을 반환하는 컨트롤러로 만들어 줌.
  - Legacy Spring 프로젝트에서 @ResponseBody를 각 메소드마다 선언했던 것을 한번에 일괄 처리.
 @GetMapping 어노테이션
  - HTTP Method인 Get의 Request를 받을 수 있는 API로 만듬.
  - Legacy Spring 프로젝트에서 @RequestMapping(method = RequestMethod.GET)으로 사용되었음.
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto (@RequestParam("name") String name,
                        @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }
}
