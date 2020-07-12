package com.example.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private Long id;
    private String text;
    private Long postId;
    private String userName;
    private Instant createdDate;

}
