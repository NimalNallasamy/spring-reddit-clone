package com.example.springredditclone.service;

import com.example.springredditclone.dto.CommentRequest;
import com.example.springredditclone.dto.CommentResponse;
import com.example.springredditclone.entity.NotificationEmail;
import com.example.springredditclone.entity.Post;
import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.mapper.CommentMapper;
import com.example.springredditclone.repository.CommentRepository;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    public static final String POST_URL = "";

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void saveComment(CommentRequest commentRequest){

        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(()->new SpringRedditException("No post found for the given postId"));
        RedditUser redditUser = authService.getCurrentUser();

        if(redditUser != null){
            commentRepository.save(commentMapper.mapToComment(commentRequest, post, redditUser));

//             Send notification to the owner of the post, that a user has commented in his post

            String message = mailContentBuilder.build(post.getRedditUser().getUserName() + " posted a comment on your post." + POST_URL);
            sendCommentNotification(message, post.getRedditUser());
        }
        else{
            throw new SpringRedditException("No Such Reddit User");
        }

    }

    private void sendCommentNotification(String message, RedditUser redditUser) {
        mailService.sendMail(new NotificationEmail(
                message,
                redditUser.getUserName()+" commented on your post",
                redditUser.getEmail()
        ));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException("No Post found for the given postId"));

        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper :: mapToCommentResponse)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByUserName(String userName) {
        RedditUser redditUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SpringRedditException("No user found for the given username"));
        return commentRepository.findAllByRedditUser(redditUser)
                .stream()
                .map(commentMapper :: mapToCommentResponse)
                .collect(Collectors.toList());
    }
}
