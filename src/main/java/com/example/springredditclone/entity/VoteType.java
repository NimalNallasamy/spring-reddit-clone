package com.example.springredditclone.entity;

import com.example.springredditclone.exception.SpringRedditException;

import java.util.Arrays;

public enum VoteType {

    UPVOTE(1),
    DOWNVOTE(-1),
    ;

    private int direction;

    private VoteType(int direction){
    }

    private Integer getDirection(){
        return direction;
    }

    public static VoteType lookUp(Integer direction){
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringRedditException("Vote not found!"));

    }

}
