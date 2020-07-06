package com.example.springredditclone.service;

import com.example.springredditclone.dto.SubRedditRequest;
import com.example.springredditclone.entity.SubReddit;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.mapper.SubRedditMapper;
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
    private final SubRedditMapper subRedditMapper;

    @Transactional
    public SubRedditRequest save(SubRedditRequest subRedditRequest){
        SubReddit savedSubReddit = subredditRepository.save(subRedditMapper.mapRequestToSubReddit(subRedditRequest));
        subRedditRequest.setId(savedSubReddit.getId());
//        subRedditRequest.setNumberOfPosts(savedSubReddit.getPosts().size());
        return subRedditRequest;
    }

    @Transactional(readOnly = true)
    public SubRedditRequest getById(Long id){
        SubReddit subReddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No Records found for the given id"));
        return subRedditMapper.mapSubRedditToRequest(subReddit);
    }

//    private SubReddit mapSubRedditRequest(SubRedditRequest subRedditRequest) {
//
//        return SubReddit.builder()
//                .name(subRedditRequest.getName())
//                .description(subRedditRequest.getDescription())
//                .build();
//
//    }

    @Transactional(readOnly = true)
    public List<SubRedditRequest> getAll(){
        return subredditRepository.findAll()
                .stream()
                .map(subRedditMapper::mapSubRedditToRequest)
                .collect(Collectors.toList());
    }

//    private SubRedditRequest mapToRequest(SubReddit subReddit) {
//
//        return SubRedditRequest.builder()
//                .name(subReddit.getName())
//                .id(subReddit.getId())
//                .numberOfPosts(subReddit.getPosts().size())
//                .build();
//
//    }
//
//    private SubReddit requestToMap(SubRedditRequest subRedditRequest){
//        return SubReddit.builder()
//                .name(subRedditRequest.getName())
//                .description(subRedditRequest.getDescription())
//                .build();
//    }

}
