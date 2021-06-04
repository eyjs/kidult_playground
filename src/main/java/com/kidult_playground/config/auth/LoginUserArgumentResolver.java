package com.kidult_playground.config.auth;

import com.kidult_playground.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
/*
  ① supportsParameter()
    - 컨트롤러 메서드의 특정 파라미터를 지원하는지 판단.
    - 여기서는 파라미터에 @LoginUser 어노테이션이 붙어 있고,
      파라미터 클래스 타입이 SessionUser.class인 경우 true를 반환.
  ② resolveArgument()
    - 파라미터에 전달할 객체를 생성
    - 여기서는 세션에서 객체를 가져옴.
 */

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {   // ①
        boolean isLoginUserAnnotation =
                    parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass =
                    SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    @Override // ②
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
