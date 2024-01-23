package org.example.springwebfluxsampleproject.service;

import lombok.RequiredArgsConstructor;
import org.example.springwebfluxsampleproject.client.PostClient;
import org.example.springwebfluxsampleproject.dto.PostResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostClient postClient;

    public Mono<PostResponse> getPostContent(Long id) {
        return postClient.getPost(id)
                .onErrorResume(error -> Mono.just(new PostResponse(id.toString(), "Fallback data %d".formatted(id))));
    }

    public Flux<PostResponse> getMultiplePostContent(List<Long> idList) {
        return  Flux.fromIterable(idList)
                .flatMap(this::getPostContent)
                .log();
    }

    public Flux<PostResponse> getParallelMultiplePostContent(List<Long> idList) {
        return  Flux.fromIterable(idList)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(this::getPostContent)
                .log()
                .sequential();
    }

}
