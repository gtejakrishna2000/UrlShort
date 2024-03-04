package com.example.UrlShort.JwtFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Define a constant string representing the secret key used for JWT token generation and validation.
// This secret key should be kept secure and not shared publicly.
// It's recommended to use a long and randomly generated string for enhanced security.
    public static final String SECRET = "c2h2aWFzYWlwYXJzaGFzb2Z0d2FyZWRldmVsb3BlckF0c3VuYmFzZQ==";


    public String extractUsername(String token) {
        // Utilize the extractClaim method to extract the subject claim from the token,
        // which typically represents the username.
        return extractClaim(token, Claims::getSubject);
    }

    // extract expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
         Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // Parse and extract all claims from the provided JWT token
        return Jwts
                // Build a JWT parser with the specified signing key
                .parserBuilder() //This starts the process of building a JWT parser.
                .setSigningKey(getSignKey())
                .build()
                // Parse the provided token and extract its body (claims)
                .parseClaimsJws(token)
                .getBody();
    }


    private Boolean isTokenExpired(String token) {
        // Extract the expiration date from the provided token
        Date expirationDate = extractExpiration(token);

        // Check if the expiration date is before the current date/time
        // If it is, the token is considered expired
        return expirationDate.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        // Extract the username from the provided token
        final String username = extractUsername(token);

        // Check if the extracted username matches the username of the UserDetails object
        // Also, ensure that the token has not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }




    public String GenerateToken(String username){
        // Print a message indicating that the GenerateToken method has been invoked
        System.out.println("GenerateToken invoked");

        // Create an empty map to hold the claims for the JWT
        Map<String, Object> claims = new HashMap<>();

        // Call the createToken method to generate a JWT with the provided username and empty claims
        return createToken(claims, username);
    }


    private String createToken(Map<String, Object> claims, String username) {
        // Build a JWT (JSON Web Token) using the provided claims and username
        return Jwts.builder()
                // Set custom claims to be included in the JWT payload
                .setClaims(claims)
                // Set the subject of the JWT, usually the username
                .setSubject(username)
                // Set the time at which the JWT was issued
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Set the expiration time for the JWT (e.g., 1 minute from the current time)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                // Sign the JWT with the specified signing key and algorithm
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                // Compact the JWT into its final string representation
                .compact();
    }


    private Key getSignKey() {
        // Decode the secret key from Base64 format to byte array
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        // Create a signing key for HMAC-SHA algorithms using the decoded byte array
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
