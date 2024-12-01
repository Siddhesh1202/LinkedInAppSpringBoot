package com.siddhesh.linkedin.posts_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikeEvent {
    Long postId;
    Long creatorId;
    Long likedByUserId;
}
