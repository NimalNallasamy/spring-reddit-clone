package com.example.springredditclone.service;

import com.example.springredditclone.dto.VoteRequest;
import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.Vote;
import com.example.springredditclone.entity.VoteType;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.SubredditRepository;
import com.example.springredditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class VoteService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final VoteRepository voteRepository;

    public void createVote(VoteRequest voteRequest){

        Post post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> new SpringRedditException("No Posts found for the given id"));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndRedditUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteRequest.getVoteType())){
            throw new SpringRedditException("You have already "+voteRequest.getVoteType()+"ed the post");
        }

        if(VoteType.UPVOTE.equals(voteRequest.getVoteType())){
            post.setVoteCount(post.getVoteCount() + 1);
        }else if(VoteType.DOWNVOTE.equals(voteRequest.getVoteType())){
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteRequest, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteRequest voteRequest, Post post) {
        return Vote.builder()
                .voteType(voteRequest.getVoteType())
                .post(post)
                .redditUser(authService.getCurrentUser())
                .build();
    }

}
