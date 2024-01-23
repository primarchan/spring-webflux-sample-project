package org.example.springwebfluxsampleproject.service;

import lombok.RequiredArgsConstructor;
import org.example.springwebfluxsampleproject.dto.UserCreateRequest;
import org.example.springwebfluxsampleproject.dto.UserUpdateRequest;
import org.example.springwebfluxsampleproject.repository.User;
import org.example.springwebfluxsampleproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> create(UserCreateRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        return userRepository.save(user);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<Integer> deleteById(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<User> update(Long id, UserUpdateRequest request) {
        return userRepository.findById(id)
                .flatMap(user -> {
                    user.setName(request.getName());
                    user.setEmail(request.getEmail());
                    return userRepository.save(user);
                });
    }

}
