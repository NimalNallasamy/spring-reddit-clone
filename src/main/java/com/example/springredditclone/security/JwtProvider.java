package com.example.springredditclone.security;

import com.example.springredditclone.entity.RedditUser;
import com.example.springredditclone.exception.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static java.util.Date.from;

@Service
public class JwtProvider {

    private KeyStore keyStore;
    @Value("${jwt.expiration.time}")
    private Long expirationTimeInMillis;

    @PostConstruct
    public void init(){
        try{
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e ) {
            throw new SpringRedditException("Exception occured while loading the keystore");
        }
    }

    public String generateToken(Authentication authentication){

        User principal = (User)authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(expirationTimeInMillis)))
                .compact();

    }

    // If we invalidate the token, then there would not be any user in the security context, so we generated one with UserName
    public String generateTokenWithUserName(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(expirationTimeInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey(){
        try{
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        }catch(Exception ex){
            throw new SpringRedditException("Exception occured while loading the keystore");
        }
    }

    /**
     * This method is used to validate the generated Jwt token.
     * If this method is executed successfully, then it means that the token is matched.
     * */
    public boolean validateJwtToken(String jwtToken){
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwtToken);
        return true;
    }

    private PublicKey getPublicKey(){
        try{
            return keyStore.getCertificate("springblog").getPublicKey();
        }
        catch(Exception ex){
            throw new SpringRedditException("Exception occured while fetching the public key from keystore");
        }
    }

    public String getUserNameFromJwtToken(String token){
        Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Long getExpirationTimeInMillis() {
        return expirationTimeInMillis;
    }
}
