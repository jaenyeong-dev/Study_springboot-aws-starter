package com.jaenyeong.dev.springboot.config.auth;

import com.jaenyeong.dev.springboot.config.auth.dto.OAuthAttributes;
import com.jaenyeong.dev.springboot.config.auth.dto.SessionUser;
import com.jaenyeong.dev.springboot.domain.user.User;
import com.jaenyeong.dev.springboot.domain.user.UserRepository;
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

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final UserRepository userRepository;
	private final HttpSession httpSession;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		/**
		 * registrationId : 현재 로그인 진행중인 서비스를 구분하는 코드
		 */
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		/**
		 * userNameAttributeName : 로그인 진행시 키가 되는 필드 값 (PK), naver, kakao 등은 미지원
		 * 구글 기본 코드 : sub
		 */
		String userNameAttributeName = userRequest
				.getClientRegistration()
				.getProviderDetails()
				.getUserInfoEndpoint()
				.getUserNameAttributeName();

		/**
		 * OAuthAttributes : OAuth2UserService를 통해서 가져온 OAuth2User의 attribute를 담은 클래스
		 */
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

		User user = saveOrUpdate(attributes);

		httpSession.setAttribute("user", new SessionUser(user));

		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey()))
				, attributes.getAttributes()
				, attributes.getNameAttributeKey());
	}

	private User saveOrUpdate(OAuthAttributes attributes) {
		User user = userRepository.findByEmail(attributes.getEmail())
				.map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
				.orElse(attributes.toEntity());

		return userRepository.save(user);
	}
}
