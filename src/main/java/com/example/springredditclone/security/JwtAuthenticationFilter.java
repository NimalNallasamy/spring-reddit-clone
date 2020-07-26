package com.example.springredditclone.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Stage 1 : We fetch the Authorization value from the header and fetch the jwt token
     * Stage 2 : Then we validate the jwt token
     * Stage 3 : Now we fetch the user details as the current user and set the user details to the front end.
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtTokenFromRequest = getJwtFromRequest(httpServletRequest);

        if(StringUtils.hasText(jwtTokenFromRequest) && jwtProvider.validateJwtToken(jwtTokenFromRequest)){
            String userName = jwtProvider.getUserNameFromJwtToken(jwtTokenFromRequest);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest httpServletRequest){

        String bearerToken = httpServletRequest.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.split(" ")[1];
        }
        return null;
    }
}
