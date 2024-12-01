package com.siddhesh.linkedin.posts_service.event;

import lombok.Builder;
import lombok.Data;

@Data
public class PostLikeEvent {
    Long postId;
    Long creatorId;
    Long likedByUserId;
}
