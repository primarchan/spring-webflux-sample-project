package org.example.springwebfluxsampleproject.service;

import lombok.RequiredArgsConstructor;
import org.example.springwebfluxsampleproject.dto.UserCreateRequest;
import org.example.springwebfluxsampleproject.dto.UserUpdateRequest;
import org.example.springwebfluxsampleproject.repository.User;
import org.example.springwebfluxsampleproject.repository.UserR2dbcRepository;
import org.example.springwebfluxsampleproject.repository.UserRepository;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserR2dbcRepository userR2dbcRepository;
    private final ReactiveRedisTemplate<String, User> reactiveRedisTemplate;


    public Mono<User> create(String name, String email) {
        User user = User.builder()
                .name(name)
                .email(email)
                .build();

        return userR2dbcRepository.save(user);
    }

    public Flux<User> findAll() {
        return userR2dbcRepository.findAll();
    }

    private String getUserCacheKey(Long id) {
        return "users:%d".formatted(id);
    }

    public Mono<User> findById(Long id) {
        // Redis 조회
        // 값이 존재하면 응답
        // 존재하지 않으면 DB 에 질의 -> 결과 Redis 에 저장
        // return userR2dbcRepository.findById(id);
        return reactiveRedisTemplate.opsForValue().get(getUserCacheKey(id))
                .switchIfEmpty(userR2dbcRepository.findById(id)
                        .flatMap(user -> reactiveRedisTemplate.opsForValue()
                                .set(getUserCacheKey(id), user, Duration.ofSeconds(30))
                                .then(Mono.just(user)))
                );
    }

    public Mono<Void> deleteById(Long id) {
        return userR2dbcRepository.deleteById(id)
                .then(reactiveRedisTemplate.unlink(getUserCacheKey(id)))
                .then(Mono.empty());
    }

    public Mono<Void> deleteByName(String name) {
        return userR2dbcRepository.deleteByName(name);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userR2dbcRepository.findById(id)
                .flatMap(user -> {
                    user.setName(name);
                    user.setEmail(email);
                    return userR2dbcRepository.save(user);
                })
                .flatMap(user -> reactiveRedisTemplate.unlink(getUserCacheKey(id))
                        .then(Mono.just(user))
                );
    }

}
