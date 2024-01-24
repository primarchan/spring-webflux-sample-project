package org.example.springwebfluxsampleproject.service;

import lombok.RequiredArgsConstructor;
import org.example.springwebfluxsampleproject.dto.UserCreateRequest;
import org.example.springwebfluxsampleproject.dto.UserUpdateRequest;
import org.example.springwebfluxsampleproject.repository.User;
import org.example.springwebfluxsampleproject.repository.UserR2dbcRepository;
import org.example.springwebfluxsampleproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserR2dbcRepository userR2dbcRepository;

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

    public Mono<User> findById(Long id) {
        return userR2dbcRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id) {
        return userR2dbcRepository.deleteById(id);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userR2dbcRepository.findById(id)
                .flatMap(user -> {
                    user.setName(name);
                    user.setEmail(email);
                    return userR2dbcRepository.save(user);
                });
    }

}
