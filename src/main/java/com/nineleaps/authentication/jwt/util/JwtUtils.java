package com.nineleaps.authentication.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.authentication.jwt.entity.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;


@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());


        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Long userId = null;
        if (userDetails instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) userDetails).getUserId(); // Cast UserDetails to User and get user ID
        }
        String rolesString = String.join(",", roles);
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("roles", rolesString)
                .withClaim("userId", userId) // Add user ID claim
                .withExpiresAt(new Date(currentTimeMillis() + 1000L * 60L * 60L * 24L * 30L))
                .sign(algorithm);
    }


    public String extractSsoUserIdentifier(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(parseToken(token));
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserMailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String extractPhoneNumber(String token) {
        return getClaimFromToken(token, "phoneNumber");
    }


    String getClaimFromToken(String token, String claimName) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(claimName, String.class);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build();
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public Boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            // Perform any additional validation checks if required
            return (decodedJWT != null && !isTokenExpired(decodedJWT));
        } catch (JWTVerificationException exception) {
            // Token verification failed
            return false;
        }
    }

    public Boolean isTokenExpired(DecodedJWT decodedJWT) {
        Date expiration = decodedJWT.getExpiresAt();
        return expiration != null && expiration.before(new Date());
    }


    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String setSecretKey(String secretKey) {
        return secretKey;
    }
}