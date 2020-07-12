package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.dto.SubRedditRequest;
import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.entity.SubReddit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    /**
     * Construct Post data from PostRequest Data.
     * */
    @Mapping(target = "id", source = "postRequest.id")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "redditUser", source = "redditUser")
    Post map(PostRequest postRequest, SubReddit subReddit, RedditUser redditUser);

//    @Mapping(target = "id", source = "postId")
//    @Mapping(target = "postName", source = "postName")
//    @Mapping(target = "url", source = "url")
//    @Mapping(target = "description", source = "description")
    @Mapping(target = "subRedditName", source = "subReddit.name")
    @Mapping(target = "userName", source = "redditUser.userName")
    PostResponse mapToDto(Post post);

}
