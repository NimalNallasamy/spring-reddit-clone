package com.example.springredditclone.controller;

import com.example.springredditclone.dto.CommentRequest;
import com.example.springredditclone.dto.CommentResponse;
import com.example.springredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest commentRequest){
        commentService.saveComment(commentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentByPostId(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByPostId(postId));
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUserName(@PathVariable String userName){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByUserName(userName));
    }


}
