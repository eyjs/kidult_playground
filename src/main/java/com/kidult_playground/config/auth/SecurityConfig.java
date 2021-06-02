package com.kidult_playground.config.auth;

import com.kidult_playground.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
  ① @EnableWebSecurity
    - Spring Security 설정들을 활성.
  ②.csrf().disable().headers().frameOptions().disable()
    - h2-console 화면을 사용하기 위한 해당 옵션들을 disable
  ③ .authorizeRequests()
    - URL별 권한 관리를 설정하는 옵션의 시작점
    - authorizeRequests가 선언되어야만 antMatchers 옵션을 사용할 수 있음.
  ④ antMatchers
    - 권한 관리 대상을 지정하는 옵션
    - URL, HTTP 메소드별로 관리가 가능.
    - "/"등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한 부여.
    - "\/api\/v1\/**\/ 주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 부여.
  ⑤ anyRequest
    - 설정된 값들 이외 나머지 URL들을 나타냄.
    - 여기서는 authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용하게 부여.
    - 인증된 사용자 즉, 로그인한 사용자들을 의미.
  ⑥ logout().logoutSuccessUrl("/")
    - 로그아웃 기능에 대한 여러 설정의 진입점.
    - 로그아웃 성공 시/ 주소로 이동.
  ⑦ oauth2.Login
    - OAuth 2 로그인 기능에 대한 여러 설정의 진입점.
  ⑧ userInfoEndPoint
    - OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당.
  ⑨ userService
    - 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
    - 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서
     추가로 진행하고자 하는 기능을 명시할 수 있다.

  ※ 스프링 시큐리티 아키텍쳐 확인 후 재 검토.
 */

@RequiredArgsConstructor
@EnableWebSecurity  // ①
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http .csrf().disable()
             .headers().frameOptions().disable() // ②
                    .and()
                        .authorizeRequests() // ③
                        .antMatchers("/", "/css/**",
                                     "/images/**", "js/**",
                                     "/h2-console/**").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // ④
                        .anyRequest().authenticated() // ⑤
                    .and()
                        .logout()
                            .logoutSuccessUrl("/") // ⑥
                    .and()
                        .oauth2Login() // ⑦
                            .userInfoEndpoint() // ⑧
                                .userService(customOAuth2UserService); // ⑨
    }
}
