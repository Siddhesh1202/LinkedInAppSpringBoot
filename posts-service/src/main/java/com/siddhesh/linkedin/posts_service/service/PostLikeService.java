package com.siddhesh.linkedin.posts_service.service;

import com.siddhesh.linkedin.posts_service.auth.UserContextHolder;
import com.siddhesh.linkedin.posts_service.entity.Post;
import com.siddhesh.linkedin.posts_service.entity.PostLike;
import com.siddhesh.linkedin.posts_service.event.PostLikeEvent;
import com.siddhesh.linkedin.posts_service.exceptions.BadRequestException;
import com.siddhesh.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.siddhesh.linkedin.posts_service.repository.PostLikeRepository;
import com.siddhesh.linkedin.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;
    private final KafkaTemplate<Long, PostLikeEvent> kafkaTemplate;

    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Liking post with id: " + postId + " by user: " + userId);
        Post post = postsRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        boolean alreadyLikes = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLikes){
            throw new BadRequestException("Post already liked");
        }
        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Liked post with id: " + postId + " by user: " + userId);

        PostLikeEvent postLikeEvent = PostLikeEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .build();

        kafkaTemplate.send("post-liked-topic", postId, postLikeEvent);
    }

    public void unLikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Liking post with id: " + postId + " by user: " + userId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) {
            throw new ResourceNotFoundException("Post does not exist");
        }
        boolean alreadyLikes = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!alreadyLikes){
            throw new BadRequestException("Post already unliked");
        }
        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Unliked post with id: " + postId + " by user: " + userId);
    }
}
