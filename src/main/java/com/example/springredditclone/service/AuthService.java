package com.example.springredditclone.service;

import com.example.springredditclone.dto.AuthenticationResponse;
import com.example.springredditclone.dto.LoginRequest;
import com.example.springredditclone.dto.RefreshTokenRequest;
import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.entity.NotificationEmail;
import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.entity.VerificationToken;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.repository.VerificationTokenRepository;
import com.example.springredditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RefreshTokenService refreshTokenService;
    /**
     * Since the Authentication Manager is an interface, we need to specify the bean that which implementation should be autowired.
     * Else Spring would throw an error.
     * */
    private final AuthenticationManager authenticationManager;
    /**
     * This is used to generate the JWT Token
     * */
    private final JwtProvider jwtProvider;

    /**
     * Since we are dealing with the DBs it would be recommended to use the @Transactional.
     * To prevent the database from facing deadlock situations.
     * */
    @Transactional
    public void signup(RegisterRequest registerRequest){

        // Sign up flow part 1 : Creating the user by storing the details of the user in the DB.
        RedditUser redditUser = new RedditUser();
        redditUser.setUserName(registerRequest.getUserName());
        redditUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        redditUser.setEmail(registerRequest.getEmail());
        redditUser.setCreated(Instant.now());
        redditUser.setEnabled(false);

        userRepository.save(redditUser);


        //Sign up flow part 2 : Generating a verification token for the user to verify his/her account.
        String token = generateVerificationToken(redditUser);

        //Sign up flow part 3 : Sending out an email to the user with the verification token for the user to verify.
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

    /**
     * Implement the logic to authenticate the user.
     * We need to store the authentication object inside the SecurityContextHolder.
     * To know if the user is logged in or not, then we need to look up the SecurityContextHolder
     * If we find the object we can be sure that the user is logged in. Else the user is not!
     *
     * Then generate the JWT token using the authentication object
     **/
    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtProvider.generateToken(authentication);
//        return new AuthenticationResponse(loginRequest.getUserName(), jwtToken);
        return  AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTimeInMillis()))
                .userName(loginRequest.getUserName())
                .build();
    }


    /**
     * This method is to fetch the user from the current session
     * */
    @Transactional
    public RedditUser getCurrentUser(){
//        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUserName((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .orElseThrow(() -> new SpringRedditException("User with the given username not present. "));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        // Validation of token, of not valid, there will be a run time exception. If nothing, the execution moves to next step.
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        String jwtToken = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUserName());
        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTimeInMillis()))
                .userName(refreshTokenRequest.getUserName())
                .build();
    }
}
