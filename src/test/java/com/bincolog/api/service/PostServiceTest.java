package com.bincolog.api.service;

import com.bincolog.api.domain.Post;
import com.bincolog.api.repository.PostRepository;
import com.bincolog.api.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목",post.getTitle());
        assertEquals("내용",post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        postRepository.save(requestPost);

        //when
        Post post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals("제목1",post.getTitle());
        assertEquals("내용1",post.getContent());
    }

}