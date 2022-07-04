package com.bincolog.api.controller;

import com.bincolog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PostController {

    // SSR -> jsp, thymeleaf, mustache, freemarker -> html rendering
    // SPA ->
    //      vue -> vue + SSR = nuxt -> javaScript _ <-> API (JSON)
    //      react -> react + SSR = next

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, CONNECT, OPTIONS, HEAD, TRACE
    // 글 등록
    // POST Method

    @PostMapping("/v1/posts")
    public String post(@RequestBody PostCreate postCreate) {
        log.info("Test params={}", postCreate.toString());
        return "Hello World";
    }

}
