package com.siddhesh.linkedin.posts_service.service;

import com.siddhesh.linkedin.posts_service.dto.PostCreateRequestDto;
import com.siddhesh.linkedin.posts_service.dto.PostDto;
import com.siddhesh.linkedin.posts_service.entity.Post;
import com.siddhesh.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.siddhesh.linkedin.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {
    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        Post savedPost = postsRepository.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with id {}", postId);
        Post post = postsRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post with id " + postId + " not found"));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostOfUser(Long userId) {
        List<Post> posts = postsRepository.findByUserId(userId);
        return posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
    }
}
