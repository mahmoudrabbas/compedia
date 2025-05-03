package com.compedia.security;

import com.compedia.entities.RefreshToken;
import com.compedia.services.RefreshTokenService;
import com.compedia.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public JwtService(
            @Value("${spring.security.jwt.secret-key}") String secretKey,
            @Value("${spring.security.jwt.access-token-validity}") long accessTokenValidity,
            @Value("${spring.security.jwt.refresh-token-validity}") long refreshTokenValidity,
            UserService userService,
            RefreshTokenService refreshTokenService
    ){
        this.secretKey = secretKey;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;

    }


    public String generateToken(UserDetails userDetails, Long expiration){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> funResolver){
        Claims claims = extractClaims(token);
        return funResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return !isTokenExpired(token) && userDetails.getUsername().equals(extractUsername(token));
    }

    public String generateAccessToken(UserDetails userDetails){
        return generateToken(userDetails, accessTokenValidity);
    }

    public String generateRefreshToken(UserDetails userDetails){
        RefreshToken refreshToken = new RefreshToken();
        String token = generateToken(userDetails, refreshTokenValidity);
        refreshToken.setExpiryDate(extractExpiration(token));
        refreshToken.setToken(token);
        refreshToken.setUser(userService.getByUsername(userDetails.getUsername()));
        refreshTokenService.saveRefreshToken(refreshToken);
        return token;
    }

}
