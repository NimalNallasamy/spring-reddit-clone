package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.dto.SubRedditRequest;
import com.example.springredditclone.entity.*;
import com.example.springredditclone.repository.CommentRepository;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.VoteRepository;
import com.example.springredditclone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.example.springredditclone.entity.VoteType.DOWNVOTE;
import static com.example.springredditclone.entity.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VoteRepository voteRepository;

    /**
     * Construct Post data from PostRequest Data.
     * */
    @Mapping(target = "id", source = "postRequest.id")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "redditUser", source = "redditUser")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "subReddit", source = "subReddit")
    public abstract Post map(PostRequest postRequest, SubReddit subReddit, RedditUser redditUser);

    @Mapping(target = "id", source = "id")
//    @Mapping(target = "postName", source = "postName")
//    @Mapping(target = "url", source = "url")
//    @Mapping(target = "description", source = "description")
    @Mapping(target = "subRedditName", source = "subReddit.name")
    @Mapping(target = "userName", source = "redditUser.userName")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post){
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post){
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) { return checkVoteType(post, UPVOTE); }
    boolean isPostDownVoted(Post post) { return checkVoteType(post, DOWNVOTE); }

    private boolean checkVoteType(Post post, VoteType voteType){
        if(authService.isLoggedIn()){
            Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndRedditUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote ->
                vote.getVoteType().equals(voteType)
            ).isPresent();
        }
        return false;
    }
}
