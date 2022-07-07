package com.bincolog.api.repository;

import com.bincolog.api.domain.Post;
import com.bincolog.api.domain.QPost;
import com.bincolog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

//    @Override
//    public List<Post> getAllPosts(int page) {
//        return jpaQueryFactory.selectFrom(QPost.post)
//                .limit(10)
//                .offset((long)(page - 1) * 10)
//                .orderBy(QPost.post.id.desc())
//                .fetch();
//    }

    @Override
    public List<Post> getAllPostsWithQueryDsl(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
