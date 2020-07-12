package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.CommentRequest;
import com.example.springredditclone.dto.CommentResponse;
import com.example.springredditclone.entity.Comment;
import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.RedditUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", source = "commentRequest.id" , ignore = true)
    @Mapping(target = "text", source = "commentRequest.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "redditUser", source = "redditUser")
    @Mapping(target = "post", source = "post")
    Comment mapToComment(CommentRequest commentRequest, Post post, RedditUser redditUser);

    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "userName", expression = "java(comment.getRedditUser().getUserName())")
    @Mapping(target = "createdDate", expression = "java(comment.getCreatedDate())")
    CommentResponse mapToCommentResponse(Comment comment);

}
