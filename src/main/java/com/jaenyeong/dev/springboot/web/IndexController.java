package com.jaenyeong.dev.springboot.web;

import com.jaenyeong.dev.springboot.config.auth.dto.SessionUser;
import com.jaenyeong.dev.springboot.service.PostsService;
import com.jaenyeong.dev.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

	private final PostsService postsService;
	private final HttpSession httpSession; // 스프링 시큐리티 적용시 추가

//	@GetMapping("/")
//	public String index() {
//		return "index";
//	}

//	@GetMapping("/")
//	public String index(Model model) {
//		model.addAttribute("posts", postsService.findAllDesc());
//		return "index";
//	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("posts", postsService.findAllDesc());
		SessionUser user = (SessionUser) httpSession.getAttribute("user");

		if (user != null) {
			model.addAttribute("userName", user.getName());
		}

		return "index";
	}

	@GetMapping("/posts/save")
	public String postsSave() {
		return "posts-save";
	}

	@GetMapping("/posts/update/{id}")
	public String postsUpdate(@PathVariable Long id, Model model) {
		PostsResponseDto dto = postsService.findById(id);
		model.addAttribute("post", dto);
		return "posts-update";
	}
}
