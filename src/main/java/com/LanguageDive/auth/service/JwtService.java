package com.LanguageDive.auth.service;

import com.LanguageDive.auth.security.UserPrincipal;
import com.LanguageDive.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String getToken(User user){
        UserPrincipal userPrincipal = new UserPrincipal(user);

        boolean isAdmin = userPrincipal.getAuthorities().stream()
                .anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));

        return Jwts.builder()
                .claim("role", isAdmin ? "ROLE_ADMIN" : "ROLE_USER")
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        if (!(user instanceof UserPrincipal userPrincipal)) {
            return false;
        }

        Long userId = getUserIdFromToken(token);
        return userId.equals(userPrincipal.getUserId());
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaim(token, Claims::getSubject));
    }

    private Claims getAllClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
