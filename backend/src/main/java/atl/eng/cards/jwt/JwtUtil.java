package atl.eng.cards.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import atl.eng.cards.model.Credential;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil  {
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.ttl.access:600000}") // default 10 minutes
    private Long ttlAccess;

    @Value("${app.jwt.ttl.refresh:3600000}") // default 1 hour
    private Long ttlRefresh;

    public String generateAccessToken(Credential credential){
        return Jwts.builder()
            .subject(credential.getUsername())
            .claim("role", credential.getRole().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + ttlAccess))
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    public String generateRefreshToken(Credential credential){
        return Jwts.builder()
            .subject(credential.getUsername())
            .claim("role", credential.getRole().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + ttlRefresh))
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    public String extractUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRoleFromToken(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role",String.class);
    }

    public Boolean isActive(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .after(new Date());
    }

    public Boolean validateToken(String token){
        try{
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return true;
        }catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
