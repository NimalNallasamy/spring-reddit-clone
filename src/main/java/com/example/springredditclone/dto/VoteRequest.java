package com.example.springredditclone.dto;

import com.example.springredditclone.entity.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {

    private VoteType voteType;
    private Long postId;

}
