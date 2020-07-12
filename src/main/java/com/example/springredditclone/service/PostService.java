package com.example.springredditclone.service;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.entity.SubReddit;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.mapper.PostMapper;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.SubredditRepository;
import com.example.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    private final PostMapper postMapper;

    public void save(PostRequest postRequest){

        SubReddit subReddit = subredditRepository.findByName(postRequest.getSubRedditName())
                .orElseThrow(() -> new SpringRedditException("Sub Reddit with the give name not found"));

        RedditUser redditUser = authService.getCurrentUser();

        postRepository.save(postMapper.map(postRequest, subReddit, redditUser));

    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No post found for the given postId"));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper :: mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubRedditId(Long id) {
        SubReddit subReddit = subredditRepository.findById(id)
                .orElseThrow(()-> new SpringRedditException("No Sub Reddit Records found for the given subReddit id"));
        return postRepository.findAllBySubReddit(subReddit)
                .stream()
                .map(postMapper :: mapToDto )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUserName(String userName) {
        RedditUser redditUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SpringRedditException("No user found with the given user name"));

        return postRepository.findByRedditUser(redditUser)
                .stream()
                .map(postMapper :: mapToDto)
                .collect(Collectors.toList());

    }
}
