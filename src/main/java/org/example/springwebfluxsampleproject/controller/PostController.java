package org.example.springwebfluxsampleproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.springwebfluxsampleproject.dto.PostResponse;
import org.example.springwebfluxsampleproject.service.PostService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public Mono<PostResponse> getPostContent(@PathVariable Long id) {
        return postService.getPostContent(id);
    }

    @GetMapping("/search-single")
    public Flux<PostResponse> getMultiPostContent(@RequestParam(name = "ids") List<Long> idList) {
        return postService.getMultiplePostContent(idList);
    }

    @GetMapping("/search-parallel")
    public Flux<PostResponse> getParallelMultiplePostContent(@RequestParam(name = "ids") List<Long> idList) {
        return postService.getParallelMultiplePostContent(idList);
    }
}
