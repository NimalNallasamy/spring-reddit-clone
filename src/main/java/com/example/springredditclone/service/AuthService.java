package com.example.springredditclone.service;

import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.entity.NotificationEmail;
import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.entity.VerificationToken;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    /**
     * Autowiring classes using @Autowired annotation is one of the practices that would follow the field injection using the getters and setters.
     * But constructor injection is the best among the 2. So we would be making use of constructor injection.
     * We will be making the variables as final and that would throw an error saying that the variables might not be declared.
     * For that we will make use of the @AllArgsConstructor by Lombok.
     * */
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    /**
     * Since we are dealing with the DBs it would be recommended to use the @Transactional.
     * To prevent the database from facing deadlock situations.
     * */
    @Transactional
    public void signup(RegisterRequest registerRequest){

        /**
         * Sign up flow part 1 : Creating the user by storing the details of the user in the DB.
         * */
        RedditUser redditUser = new RedditUser();
        redditUser.setUserName(registerRequest.getUserName());
        redditUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        redditUser.setEmail(registerRequest.getEmail());
        redditUser.setCreated(Instant.now());
        redditUser.setEnabled(false);

        userRepository.save(redditUser);

        /**
         * Sign up flow part 2 : Generating a verification token for the user to verify his/her account.
         * */

        String token = generateVerificationToken(redditUser);

        /**
         * Sign up flow part 3 : Sending out an email to the user with the verification token for the user to verify.
         * */
        String authUrl = "https://localhost:8080/api/auth/accountVerification/"+token;
        mailService.sendMail(new NotificationEmail(
                "Please click on the URL to activate the account : "+authUrl,
                "Please Activate your Account",
                redditUser.getEmail()

        ));
    }

    private String generateVerificationToken(RedditUser redditUser){

        // UUID.randomUUID will be generating unique and random 128 bit string.
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setRedditUser(redditUser);
//        verificationToken.setExpiryDate(Instant.now().plus(TemporalAmount));

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {

        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));

        // Fetch and Verify the User
        fetchAndEnableUser(verificationToken.get());

    }

    @Transactional
    public void fetchAndEnableUser(VerificationToken verificationToken){
        String userName = verificationToken.getRedditUser().getUserName();

        Optional<RedditUser> redditUser = userRepository.findByUserName(userName);
        redditUser.orElseThrow(() -> new SpringRedditException("Invalid User Name"));

        RedditUser user = redditUser.get();

        user.setEnabled(true);

        userRepository.save(user);
    }
}
