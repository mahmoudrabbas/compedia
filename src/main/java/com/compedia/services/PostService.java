package com.compedia.services;

import com.compedia.DTOs.PostDTO;
import com.compedia.entities.PostEntity;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // get all posts
    public List<PostEntity> getAllPosts(){
        return postRepository.findAll();
    }

    // get post by id
    public PostEntity getPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post Not Found with id: "+id));
    }

    // add post
    public PostEntity addPost(PostDTO postDTO){
        PostEntity post = new PostEntity().mapToEntity(postDTO);
        return postRepository.save(post);
    }

    // update post
    public PostEntity updatePost(PostDTO postDTO){
        PostEntity returnedPost = postRepository.findById(postDTO.getId())
                .orElseThrow(() -> new NotFoundException("Post Not Found with id: "+postDTO.getId()));

        returnedPost.setPostContent(postDTO.getPostContent());
        returnedPost.setUser(postDTO.getUser());

        return postRepository.save(returnedPost);
    }

    // delete post
    public int deletePostById(Long id){
        PostEntity post = postRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Post Not Found To Delete with Id: "+id));
        postRepository.delete(post);
        return 1;
    }
}
