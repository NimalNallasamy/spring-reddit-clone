package com.example.springredditclone.controller;

import com.example.springredditclone.dto.SubRedditRequest;
import com.example.springredditclone.entity.SubReddit;
import com.example.springredditclone.service.SubRedditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subReddit")
@AllArgsConstructor
public class SubRedditController {

    private final SubRedditService subRedditService;

    @PostMapping
    public ResponseEntity<SubRedditRequest> createSubReddit(@RequestBody SubRedditRequest subRedditRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(subRedditService.save(subRedditRequest));
    }

    @GetMapping
    public ResponseEntity<List<SubRedditRequest>> getAllSubReddits(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subRedditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditRequest> getSubRedditById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subRedditService.getById(id));
    }

}
