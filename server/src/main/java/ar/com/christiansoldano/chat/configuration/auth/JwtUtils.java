package ar.com.christiansoldano.chat.configuration.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${application.security.jwt-secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt-expiration}")
    private long JWT_EXPIRATION;

    @Value("${application.security.jwt-refresh-expiration}")
    private long JWT_REFRESH_EXPIRATION;

    private static final String JWT_TYPE_ACCESS = "access";
    private static final String JWT_TYPE_REFRESH = "refresh";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <R> R extractClaim(String token, Function<Claims, R> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        Map<String, Object> claims = Map.of(
                "roles", roles,
                "type", JWT_TYPE_ACCESS
        );
        return buildToken(claims, user, JWT_EXPIRATION);
    }

    public String generateRefreshToken(UserDetails user) {
        return buildToken(Map.of("type", JWT_TYPE_REFRESH), user, JWT_REFRESH_EXPIRATION);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails user, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        String username = extractUsername(token);
        String type = (String) extractAllClaims(token).get("type");
        return (username.equals(user.getUsername())) && !isTokenExpired(token) && type.equals(JWT_TYPE_ACCESS);
    }

    public boolean isRefreshToken(String token) {
        String type = (String) extractAllClaims(token).get("type");
        return type.equals(JWT_TYPE_REFRESH);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
