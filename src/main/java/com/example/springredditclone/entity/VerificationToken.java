package com.example.springredditclone.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VERIFICATION_TOKEN")
@Builder
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token")
    @NotBlank(message = "Token can not be empty")
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    private RedditUser redditUser;
    @Column(name = "expiryDate")
    private Instant expiryDate;

}
