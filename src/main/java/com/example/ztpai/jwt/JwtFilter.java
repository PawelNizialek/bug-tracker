package com.example.ztpai.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.jsonwebtoken.Jwts.parser;

public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    String publicKey = "ztpaiztpaiztpai123ztpaiztpaiztpaiztpai123ztpaiztpaiztpaiztpai123ztpaiztpaiztpaiztpai123ztpai";

    public JwtFilter(@Qualifier("myUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String JwtToken = prepareToken(httpServletRequest.getHeader("Authorization"));
        validateJwt(JwtToken);
        setAuthenticatedUser(getUsername(JwtToken), httpServletRequest);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    private String prepareToken(String token){
        if(Strings.isNullOrEmpty(token) || !token.startsWith("Bearer "))
            return token;
        return token.replace( "Bearer ", "");
    }
    private void validateJwt(String token){
        parser().setSigningKey(Keys.hmacShaKeyFor(publicKey.getBytes())).parseClaimsJws(token);
    }
    private String getUsername(String token){
        return parser()
                .setSigningKey(Keys.hmacShaKeyFor(publicKey.getBytes()))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    private void setAuthenticatedUser(String username, HttpServletRequest httpServletRequest){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken
                authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
