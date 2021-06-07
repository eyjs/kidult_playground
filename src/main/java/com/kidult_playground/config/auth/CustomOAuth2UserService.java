package com.kidult_playground.config.auth;

import com.kidult_playground.config.auth.dto.OAuthAttributes;
import com.kidult_playground.config.auth.dto.SessionUser;
import com.kidult_playground.domain.user.User;
import com.kidult_playground.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
/*
  ※ 추가 학습 필요한 항목.
    - 설정 흐름도 확인 필요.
    - 각 설정 별 목적 정리.
    - 각 설정 별 세부 내용.
  ① registrationId
    - 현재 로그인 진행 중인 서비스를 구분하는 코드.
    - 지금은 구글만 사용하는 불필요한 값이지만,
      이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
  ② userNameAttributeName
    - OAuth2 로그인 진행 시 키가 되는 필드값을 이야기함.
    - Primary Key와 같은 의미
    - 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 지원하지 않음.
      -> 구글의 기본 코드는 "sub"
    - 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용.
  ③ OAuthAttributes
    - OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
    - 이후 네이버 등 다른 소셜 로그인도 이 클래스 사용.
  ④ SessionUser
    - 세션에 사용자 정보를 저장하기 위한 Dto 클래스
    - 왜 User 클래스를 쓰지 않고 새로 만들어서 쓰는지는 확인 필요.
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    // 큰 틀로는 접속한 유저 Request 객체 얻어서 ID, PW, Map<String, Object> getAttributes() 객체 Get,
    // OAuthAttributes 객체 of() Builder 시킴 -> ofGoogle() -> 구글용 User로
    // Build된 OAuthAttributes객체 .toEntity() 호출 시 구글용 유저 Get
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User>
                delegate = new DefaultOAuth2UserService();  // RestTemplate Get
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId =
                userRequest.getClientRegistration().getRegistrationId(); // ①
        String userNameAttributeName =
                userRequest.getClientRegistration().getProviderDetails().
                        getUserInfoEndpoint().getUserNameAttributeName(); // ②
        OAuthAttributes attributes =
                OAuthAttributes.of(registrationId, userNameAttributeName,
                                   oAuth2User.getAttributes()); // ③
        User user = saveOrUpdate(attributes);   // 기존유저일 경우 getName, getPicture
                                                // 신규유저일 경우 attributes.toEntity() 호출
        httpSession.setAttribute("user", new SessionUser(user)); // ④ 세션 Dto 인증 완료 된 유저객체 저장.

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRoleKey())), // 유저 권한 (규칙 : ROLE_ 접두어)
                            attributes.getAttributes(),
                            attributes.getNameAttributeKey());
        // Front로 위 작업내용 반영
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user =
                userRepository.findByEmail(attributes.getEmail())
                            .map(entity ->
                                    entity.update(attributes.getName(), attributes.getPicture()))
                                    .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
