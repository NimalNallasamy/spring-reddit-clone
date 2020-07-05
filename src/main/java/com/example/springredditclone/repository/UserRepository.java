package com.example.springredditclone.repository;

import com.example.springredditclone.entity.RedditUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<RedditUser, Long> {

    Optional<RedditUser> findByUserName(String userName);

}
