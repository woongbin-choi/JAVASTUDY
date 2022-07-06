package com.bincolog.api.service;

import com.bincolog.api.domain.Post;
import com.bincolog.api.repository.PostRepository;
import com.bincolog.api.request.PostCreate;
import com.bincolog.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        // 클라이언트 요구사항
        // json 응답에서 title 값 길이를 최대 10글자로 해주세요.(서버에서 해야만 하는 경우라면)

        //when
        PostResponse response = postService.get(requestPost.getId());

        //then
        assertNotNull(response);
        assertEquals("제목1",response.getTitle());
        assertEquals("내용1",response.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
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

        Pageable pageable = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC, "id"));

        //when
        List<PostResponse> posts = postService.getAllPosts(pageable);

        //then
        assertEquals(5L, posts.size());
        assertEquals("빈코 제목 = 30", posts.get(0).getTitle());
        assertEquals("빈코 제목 = 26", posts.get(4).getTitle());
    }

}