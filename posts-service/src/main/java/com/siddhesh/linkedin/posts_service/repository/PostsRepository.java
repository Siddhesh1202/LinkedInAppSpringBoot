package com.siddhesh.linkedin.posts_service.repository;

import com.siddhesh.linkedin.posts_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}
