package com.bincolog.api.repository;

import com.bincolog.api.domain.Post;
import com.bincolog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getAllPostsWithQueryDsl(PostSearch postSearch);
}
