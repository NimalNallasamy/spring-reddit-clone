package com.example.springredditclone.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REDDIT_USER")
@Builder
public class RedditUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userName")
    @NotBlank(message = "User Name is required")
    private String userName;
    @Column(name = "password")
    @NotBlank(message = "Password is required")
    private String password;
    @Column(name = "email")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @Column(name = "created")
    private Instant created;
    @Column(name = "enabled")
    private boolean enabled;

}
