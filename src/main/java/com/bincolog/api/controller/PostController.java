package com.bincolog.api.controller;

import com.bincolog.api.request.PostCreate;
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
public class PostController {

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

    @PostMapping("/posts")
    public Map<String, String > post(@RequestBody @Valid PostCreate postCreate, BindingResult bindingResult) throws Exception {
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

}
