package com.jaenyeong.dev.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaenyeong.dev.springboot.domain.posts.Posts;
import com.jaenyeong.dev.springboot.domain.posts.PostsRepository;
import com.jaenyeong.dev.springboot.web.dto.PostsSaveRequestDto;
import com.jaenyeong.dev.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

	@LocalServerPort
	private int port;

	/**
	 * private TestRestTemplate restTemplate
	 * MockMvc를 사용한 테스트로 변경하여 주석처리
	 */
//	@Autowired
//	private TestRestTemplate restTemplate;

	@Autowired
	private PostsRepository postsRepository;

	/**
	 * @SpringBootTest에서 MockMvc를 사용하기 위해 추가
	 * private WebApplicationContext context
	 * private MockMvc mvc
	 * public void setup()
	 */
	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@After
	public void tearDown() throws Exception {
		postsRepository.deleteAll();
	}

//    @Test
//    public void add_posts_ok() {
//        // given
//        String title = "title";
//        String content = "content";
//        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
//                .title(title)
//                .content(content)
//                .author("author")
//                .build();
//
//        String url = "http://localhost:" + port + "/api/v1/posts";
//
//        // when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//
//        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
//
//        List<Posts> all = postsRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo(title);
//        assertThat(all.get(0).getContent()).isEqualTo(content);
//    }

	@Test
	/**
	 * @WithMockUser(roles = "USER")
	 * 가짜 목 사용자를 생성, 사용
	 * ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과
	 */
	@WithMockUser(roles = "USER")
	public void add_posts_ok() throws Exception {
		// given
		String title = "title";
		String content = "content";
		PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
				.title(title)
				.content(content)
				.author("author")
				.build();

		String url = "http://localhost:" + port + "/api/v1/posts";

		// when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
		mvc.perform(
				post(url)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(new ObjectMapper().writeValueAsString(requestDto))
		).andExpect(status().isOk());

		// then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

		List<Posts> all = postsRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(title);
		assertThat(all.get(0).getContent()).isEqualTo(content);
	}

//	  @Test
//    public void update_posts_ok() throws Exception {
//        // given
//        Posts savedPosts = postsRepository.save(
//                Posts.builder()
//                        .title("title")
//                        .content("content")
//                        .author("author").build());
//
//        Long updateId = savedPosts.getId();
//        String expectedTitle = "title2";
//        String expectedContent = "content2";
//
//        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
//                .title(expectedTitle)
//                .content(expectedContent).build();
//
//        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
//
//        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
//
//        // when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
//
//        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
//
//        List<Posts> all = postsRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
//        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
//    }

	@Test
	/**
	 * @WithMockUser(roles = "USER")
	 * 가짜 목 사용자를 생성, 사용
	 * ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과
	 */
	@WithMockUser(roles = "USER")
	public void update_posts_ok() throws Exception {
		// given
		Posts savedPosts = postsRepository.save(
				Posts.builder()
						.title("title")
						.content("content")
						.author("author").build());

		Long updateId = savedPosts.getId();
		String expectedTitle = "title2";
		String expectedContent = "content2";

		PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
				.title(expectedTitle)
				.content(expectedContent)
				.build();

		String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

//		HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

		// when
//		ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
		mvc.perform(
				put(url)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(new ObjectMapper().writeValueAsString(requestDto))
		).andExpect(status().isOk());

		// then
//		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(responseEntity.getBody()).isGreaterThan(0L);

		List<Posts> all = postsRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
	}
}
