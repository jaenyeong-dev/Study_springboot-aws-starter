package com.jaenyeong.dev.springboot.web;

import com.jaenyeong.dev.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
/**
 * @WebMvcTest는 CustomOAuth2UserService를 스캔하지 않아 에러 발생
 * 이유는 WebSecurityConfigurerAdapter, WebMvcConfigurer, @ControllerAdvice, @Controller를 읽지만
 * @Repository, @Service, @Component는 스캔 대상이 아님
 * 그래서 SecurityConfig는 읽었으나 내부에 필요한 CustomOAuth2UserService는 읽을 수 없어 에러 발생
 * 따라서 스캔대상에서 SecurityConfig를 제거
 */
@WebMvcTest(
		controllers = HelloController.class,
		excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class HelloControllerTest {

	@Autowired
	private MockMvc mvc;

//	@Test
//	public void return_hello() throws Exception {
//		String hello = "hello";
//
//		mvc.perform(get("/hello"))
//				.andExpect(status().isOk())
//				.andExpect(content().string(hello));
//	}

	@WithMockUser(roles = "USER")
	@Test
	public void return_hello() throws Exception {
		String hello = "hello";

		mvc.perform(get("/hello"))
				.andExpect(status().isOk())
				.andExpect(content().string(hello));
	}

//	@Test
//	public void return_helloDto() throws Exception {
//		String name = "hello";
//		int amount = 1000;
//
//		mvc.perform(
//				get("/hello/dto")
//						.param("name", name)
//						.param("amount", String.valueOf(amount)))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.name", is(name)))
//				.andExpect(jsonPath("$.amount", is(amount)));
//	}

	@WithMockUser(roles = "USER")
	@Test
	public void return_helloDto() throws Exception {
		String name = "hello";
		int amount = 1000;

		mvc.perform(
				get("/hello/dto")
						.param("name", name)
						.param("amount", String.valueOf(amount)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(name)))
				.andExpect(jsonPath("$.amount", is(amount)));
	}

}
