package com.example.springredditclone.service;

import com.example.springredditclone.dto.SubRedditRequest;
import com.example.springredditclone.entity.SubReddit;
import com.example.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class SubRedditService {

    private final SubredditRepository subredditRepository;

    @Transactional
    public SubRedditRequest save(SubRedditRequest subRedditRequest){
        SubReddit savedSubReddit = subredditRepository.save(mapSubRedditRequest(subRedditRequest));
        subRedditRequest.setId(savedSubReddit.getId());
        subRedditRequest.setNumberOfPosts(savedSubReddit.getPosts().size());
        return subRedditRequest;
    }

    private SubReddit mapSubRedditRequest(SubRedditRequest subRedditRequest) {

        return SubReddit.builder()
                .name(subRedditRequest.getSubRedditName())
                .description(subRedditRequest.getDescription())
                .build();

    }

    @Transactional(readOnly = true)
    public List<SubRedditRequest> getAll(){
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToRequest)
                .collect(Collectors.toList());
    }

    private SubRedditRequest mapToRequest(SubReddit subReddit) {

        return SubRedditRequest.builder()
                .subRedditName(subReddit.getName())
                .id(subReddit.getId())
                .numberOfPosts(subReddit.getPosts().size())
                .build();

    }

    private SubReddit requestToMap(SubRedditRequest subRedditRequest){
        return SubReddit.builder()
                .name(subRedditRequest.getSubRedditName())
                .description(subRedditRequest.getDescription())
                .build();
    }

}
