package com.jaenyeong.dev.springboot.web;

import com.jaenyeong.dev.springboot.service.PostsService;
import com.jaenyeong.dev.springboot.web.dto.PostsResponseDto;
import com.jaenyeong.dev.springboot.web.dto.PostsSaveRequestDto;
import com.jaenyeong.dev.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto reqeustDto) {
        return postsService.update(id, reqeustDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }
}
