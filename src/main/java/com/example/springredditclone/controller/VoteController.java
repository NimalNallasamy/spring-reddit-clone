package com.example.springredditclone.controller;

import com.example.springredditclone.dto.VoteRequest;
import com.example.springredditclone.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> createVote(@RequestBody VoteRequest voteRequest){
        voteService.createVote(voteRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public List<VoteRequest> getAllVotes(){
        return  null;
    }

}
