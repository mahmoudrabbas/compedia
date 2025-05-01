package com.compedia.services;

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
    public PostEntity addPost(PostEntity post){
        return postRepository.save(post);
    }

    // update post
    public PostEntity updatePost(PostEntity post){
        PostEntity returnedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new NotFoundException("Post Not Found with id: "+post.getId()));

        returnedPost.setPostContent(post.getPostContent());
        returnedPost.setUser(post.getUser());

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
