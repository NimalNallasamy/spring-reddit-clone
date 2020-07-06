package com.example.springredditclone.mapper;

import com.example.springredditclone.dto.SubRedditRequest;
import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.SubReddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubRedditMapper {

    /**
     * This method converts the data from Entity(DAO) to DTO.
     * Since the fields like id, name, description have the same name in DTO and Entity, they will be  without any issues
     * To map the numberOfPosts field in DTO to list of Posts in Entity, we make use of the @Mapping annotation and the target as numberOfPosts
     * To map that we need to write some custom expression named mapPosts, passing the list of posts from Entity(Dao)
     * And fetching the size, returning the same.
     * Which would be mapped to DTO field.
     * */
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subReddit.getPosts()))")
    SubRedditRequest mapSubRedditToRequest(SubReddit subReddit);

    default Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }

    /**
     * This method is used to do the inverse mapping from dto to dao, but what we need to do is that we need to ignore the posts field
     * So we have the key ignore, which we mark as true
     * */
    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    SubReddit mapRequestToSubReddit(SubRedditRequest subRedditRequest);
}
