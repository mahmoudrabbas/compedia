package com.compedia.controllers;

import com.compedia.DTOs.PostDTO;
import com.compedia.entities.PostEntity;
import com.compedia.repositories.PostRepository;
import com.compedia.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getAllPosts(){
        return ResponseEntity.ok().body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok().body(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<?> add(PostDTO post){
        return ResponseEntity.ok().body(postService.addPost(post));
    }

    @PutMapping
    public ResponseEntity<?> update(PostDTO post){
        return ResponseEntity.ok().body(postService.updatePost(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok().body(postService.deletePostById(id));
    }

}
