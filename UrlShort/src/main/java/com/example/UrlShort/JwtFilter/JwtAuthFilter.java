package com.example.UrlShort.JwtFilter;

import com.example.UrlShort.SecurityFilter.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserDetailsServiceImp userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the Authorization header from the request
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // Extract the token from the header
            token = header.substring(7);
            // Extract the username from the token
            username = jwtService.extractUsername(token);
        }

        // If username is extracted and no authentication object exists in the SecurityContextHolder
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details by username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token with user details
            if (jwtService.validateToken(token, userDetails)) {
                // Create an authentication token using user details
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Set additional details for the authentication token like ip address
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication token in the SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue the filter chain
            filterChain.doFilter(request, response);
    }



}
