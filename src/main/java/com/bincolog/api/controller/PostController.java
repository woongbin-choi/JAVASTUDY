package com.bincolog.api.controller;

import com.bincolog.api.domain.Post;
import com.bincolog.api.request.PostCreate;
import com.bincolog.api.response.PostResponse;
import com.bincolog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // SSR -> jsp, thymeleaf, mustache, freemarker -> html rendering
    // SPA ->
    //      vue -> vue + SSR = nuxt -> javaScript _ <-> API (JSON)
    //      react -> react + SSR = next

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, CONNECT, OPTIONS, HEAD, TRACE
    // 글 등록
    // POST Method

    // 데이터를 검증하는 이유
    // 1. client 개발자가 깜빡하고 잊어버릴 수 있다. 실수로 값을 안보낼 수도 있다.
    // 2. client bug로 값이 누락될 수 있다.
    // 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
    // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
    // 5. 서버 개발자의 편의를 위해서

    @PostMapping("/posts-test")
    public String postTest(@RequestBody PostCreate postCreate) throws Exception {
        log.info("Test params={}", postCreate.toString());

        String title = postCreate.getTitle();
        if(title == null || title.equals("")){
            // 1. 노가다
            // 2. 개발 팁 -> 무언가 3번 이상 반복작업을 할 때 내가 뭔가 잘못하고 있는건 아닐지 의심한다.
            // 3. 누락될 가능성이 있다.
            // 4. 생각보다 검증해야할 게 많다. (꼼꼼하지 않을 수 있다)
            // 5. 뭔가 개발자 스럽지 않다.
            throw new Exception("타이틀값이 없다!");
        }

        String content = postCreate.getContent();
        if (content == null || content.equals("")){

        }
        return "Hello World";
    }

    @PostMapping("/posts-test2")
    public Map<String, String > postTest2(@RequestBody @Valid PostCreate postCreate, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()){
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            FieldError firstFieldError = fieldErrorList.get(0);
            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }
        return null;
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate postCreate){
        // POST -> 200, 201
        // Case1. 저장한 데이터 Entity -> response 응답
        // Case2. 저장한 데이터 PK만 응답
        // Case3. 응답 필요 없음 -> void
        postService.write(postCreate);
    }

    /*
        /posts -> 글 전체 조회(검색 + 페이징)
        /posts/{postId} -> 글 한개만 조회

        // Request Class -> PostCreate
        // Response Class -> PostResponse
     */

    @GetMapping("/posts")
    public List<PostResponse> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        // 서비스 정책에 맞는 응답 클래스를 분리해야 한다.
        return postService.get(postId);
    }

}
