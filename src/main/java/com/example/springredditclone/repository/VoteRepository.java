package com.example.springredditclone.repository;

import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndRedditUserOrderByVoteIdDesc(Post post, RedditUser redditUser);

}
