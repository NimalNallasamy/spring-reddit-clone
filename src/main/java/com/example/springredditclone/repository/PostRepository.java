package com.example.springredditclone.repository;

import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.entity.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubReddit(SubReddit subReddit);

    List<Post> findByRedditUser(RedditUser redditUser);

}
