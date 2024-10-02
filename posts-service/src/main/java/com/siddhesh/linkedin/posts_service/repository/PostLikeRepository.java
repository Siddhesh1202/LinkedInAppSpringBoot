package com.siddhesh.linkedin.posts_service.repository;

import com.siddhesh.linkedin.posts_service.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    @Transactional
    void deleteByUserIdAndPostId(long userId, Long postId);
}
