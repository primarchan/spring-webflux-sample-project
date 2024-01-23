package org.example.springwebfluxsampleproject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(UserController.class)
class UserControllerTest {

    private WebTestClient webTestClient;

    @Test
    void createUser() {
    }

    @Test
    void findAllUser() {
    }

    @Test
    void findUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }

}