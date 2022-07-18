package com.bincolog.api.repository;

import com.bincolog.api.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
