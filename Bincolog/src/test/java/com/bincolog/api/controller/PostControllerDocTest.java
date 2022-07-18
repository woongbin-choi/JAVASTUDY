package com.bincolog.api.controller;

import com.bincolog.api.domain.Post;
import com.bincolog.api.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    private MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

//    @BeforeEach
//    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }
//
//    @Test
//    @DisplayName("글 단건 조회 테스트")
//    void test1() throws Exception{
//        // given
//        Post post = Post.builder()
//                .title("foo123412341234")
//                .content("bar")
//                .build();
//        postRepository.save(post);
//
//        //expected
//        this.mockMvc.perform(get("/posts/{postId}",1L).accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andDo(document("index"));
//    }

    // import가 안되고 있음. 테스트 실행하면 build-generated_snippets에 파일이 나옴.
}
