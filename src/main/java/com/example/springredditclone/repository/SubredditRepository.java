package com.example.springredditclone.repository;

import com.example.springredditclone.entity.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SubredditRepository extends JpaRepository<SubReddit, Long> {

    Optional<SubReddit> findByName(String subRedditName);

    Optional<SubReddit> findById(Long id);

}
