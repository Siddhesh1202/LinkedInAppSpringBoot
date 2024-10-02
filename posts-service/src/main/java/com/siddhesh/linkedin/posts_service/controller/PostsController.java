package com.siddhesh.linkedin.posts_service.controller;

import com.siddhesh.linkedin.posts_service.dto.PostCreateRequestDto;
import com.siddhesh.linkedin.posts_service.dto.PostDto;
import com.siddhesh.linkedin.posts_service.entity.Post;
import com.siddhesh.linkedin.posts_service.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, HttpServletRequest httpServletRequest) {
        PostDto createdPost = postsService.createPost(postCreateRequestDto, 1L);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        PostDto postDto = postsService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(@PathVariable Long userId) {
        List<PostDto> postDtoList = postsService.getAllPostOfUser(userId);
        return ResponseEntity.ok(postDtoList);
    }
}
