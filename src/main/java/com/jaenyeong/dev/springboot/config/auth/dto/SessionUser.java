package com.jaenyeong.dev.springboot.config.auth.dto;

import com.jaenyeong.dev.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
	/**
	 * 로그인 처리시 User 클래스를 직접 사용하지 않고 SessionUser 클래스를 사용하는 이유
	 * User 클래스(엔티티이기 때문에)가 다른 엔티티 클래스와 어떤 관계가 형성될지 모름
	 * 따라서 무턱대고 엔티티를 직렬화하여 사용하면 서브 클래스 또한 포함, 성능이슈와 같은 부수효과가 발생할 확률이 있음
	 */
	private String name;
	private String email;
	private String picture;

	public SessionUser(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.picture = user.getPicture();
	}
}
