package org.example.springwebfluxsampleproject.service;

import lombok.RequiredArgsConstructor;
import org.example.springwebfluxsampleproject.repository.Post;
import org.example.springwebfluxsampleproject.repository.PostR2dbcRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostServiceV2 {

    private final PostR2dbcRepository postR2dbcRepository;

    public Mono<Post> create(Long userId, String title, String content) {
        Post post = Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .build();

        return postR2dbcRepository.save(post);
    }

    public Flux<Post> findAll() {
        return postR2dbcRepository.findAll();
    }

    public Mono<Post> findById(Long id) {
        return postR2dbcRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id) {
        return postR2dbcRepository.deleteById(id);
    }

    public Flux<Post> findAllByUserId(Long id) {
        // return postR2dbcRepository.findByUserId(id);
        return postR2dbcRepository.findAllByUserId(id);
    }

}
