package com.tracker.taskstracker.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.tracker.taskstracker.model.response.UserResponseModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

    private static final long JWT_TOKEN_EXPIRATION = System.currentTimeMillis() + 1000 * 60 * 60 * 24; // 24 hours
    private static final String SECRET = "top-secret";

    public static String generateToken(UserResponseModel userResponseModel) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userResponseModel.getRoles());
        return createToken(claims, userResponseModel.getUsername());
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(subject)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(JWT_TOKEN_EXPIRATION))
                   .signWith(SignatureAlgorithm.HS256, SECRET)
                   .compact();
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .setSigningKey(SECRET)
                   .parseClaimsJws(token)
                   .getBody();
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
