package com.bincolog.api.controller;

import com.bincolog.api.domain.Post;
import com.bincolog.api.repository.PostRepository;
import com.bincolog.api.request.PostCreate;
import com.bincolog.api.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    void printTest() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);


        // 글 제목, 글 내용, 사용자(id,name 등) <- 이렇게 되기 때문에 JSON 데이터를 사용
        //application, json 형태로 보냄(MockMvc)
        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청 시 title값은 필수다.")
    void validChk() throws Exception {
        PostCreate request = PostCreate.builder()
                .title("")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청 시 DB에 값이 저장된다.")
    void insertPost() throws Exception {
        // give
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void test4() throws Exception{
        // given
        Post post = Post.builder()
                .title("foo123412341234")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo1234123"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception{
        // given
        Post post1 = Post.builder()
                .title("1")
                .content("11")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("2")
                .content("22")
                .build();
        postRepository.save(post2);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id").value(post1.getId()))
                .andExpect(jsonPath("$[0].title").value("1"))
                .andExpect(jsonPath("$[0].content").value("11"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test6() throws Exception{
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("빈코 제목 = " + i)
                                .content("빈코 내용 = " + i)
                                .build()
                )
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&sort=id,desc")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(5)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$.[0].title").value("빈코 제목 = 30"))
                .andExpect(jsonPath("$.[0].content").value("빈코 내용 = 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception{
        //given
        Post post = Post.builder()
                .title("빈코 제목")
                .content("빈코 내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("빈코 제목 수정")
                .content("빈코 내용")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("빈코 제목")
                .content("빈코 내용")
                .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하는 글 조회")
    void test9() throws Exception{
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하는 글 수정")
    void test10() throws Exception{
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("빈코 제목 수정")
                .content("빈코 내용")
                .build();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
