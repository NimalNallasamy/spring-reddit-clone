package com.example.springredditclone.repository;

import com.example.springredditclone.entity.Comment;
import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.RedditUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByRedditUser(RedditUser redditUser);

}
