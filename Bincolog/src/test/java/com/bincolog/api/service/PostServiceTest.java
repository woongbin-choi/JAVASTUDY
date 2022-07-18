package com.bincolog.api.service;

import com.bincolog.api.domain.Post;
import com.bincolog.api.exception.PostNotFound;
import com.bincolog.api.repository.PostRepository;
import com.bincolog.api.request.PostCreate;
import com.bincolog.api.request.PostEdit;
import com.bincolog.api.request.PostSearch;
import com.bincolog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
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
    @DisplayName("글 한개 조회 예외처리")
    void test2_1() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        //expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
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

        Pageable pageable = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"id"));

        //when
        List<PostResponse> posts = postService.getAllPosts(pageable);

        //then
        assertEquals(5L, posts.size());
        assertEquals("빈코 제목 = 30", posts.get(0).getTitle());
        assertEquals("빈코 제목 = 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("QueryDsl 테스트")
    void test4() {
        //given
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i ->
                        Post.builder()
                                .title("빈코 제목 = " + i)
                                .content("빈코 내용 = " + i)
                                .build()
                )
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //when
        List<PostResponse> posts = postService.getAllPostsWithQueryDsl(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("빈코 제목 = 19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test5() {
        //given
        Post post = Post.builder()
                .title("빈코 제목")
                .content("빈코 내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("빈코 제목수정")
                .content("빈코 내용")
                .build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        Assertions.assertEquals("빈코 제목수정", changePost.getTitle());
        Assertions.assertEquals("빈코 내용", changePost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6() {
        //given
        Post post = Post.builder()
                .title("빈코 제목")
                .content("빈코 내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("빈코 제목")
                .content("빈코 내용 수정")
                .build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        Assertions.assertEquals("빈코 제목", changePost.getTitle());
        Assertions.assertEquals("빈코 내용 수정", changePost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test6_1() {
        //given
        Post post = Post.builder()
                .title("빈코 제목")
                .content("빈코 내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("빈코 제목")
                .content("빈코 내용 수정")
                .build();

        //expected
        Assertions.assertThrows(PostNotFound.class, () -> {
           postService.edit(post.getId() + 1L, postEdit);
        });
    }

    @Test
    @DisplayName("게시글 삭제")
    void test7() {
        //given
        Post post = Post.builder()
                .title("빈코 제목")
                .content("빈코 내용")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        Assertions.assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        //expected
        Assertions.assertThrows(PostNotFound.class, () -> {
           postService.delete(post.getId() + 1L);
        });
    }

}