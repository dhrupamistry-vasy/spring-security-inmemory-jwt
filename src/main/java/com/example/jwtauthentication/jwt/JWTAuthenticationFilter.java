package com.example.jwtauthentication.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Bearer
        // pass token in headers from the postman request
        // Header
        final String requestHeader = request.getHeader("Authorization");
        System.out.println("Headers: {} " + requestHeader);
        // extracting the
        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            // start with the bearer
            // extract token after 7 index
            token = requestHeader.substring(7);
            try {
                // get the username from the token using jwtHelper class
                username = jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                System.out.println("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                System.out.println("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Header Value !! ");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // fetch user detail from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // checking token validity
            // checking token basis on the user details service
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {
                // set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Validation fails !!");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/authenticate");
    }
}