package com.app.core.api.shared.infrastructure.adapters.out.auth;

import com.app.core.api.shared.exception.JwtException;
import com.app.core.api.user.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Serviço responsável pela geração, validação e extração de informações do token JWT.
 */
@Service
public class JwtTokenProvider {

    @Value("${jwt.secret_key}")
    private String secret;

    @Value("${jwt.expiration}")
    private Integer expiration;

    private SecretKey key;

    /**
     * Inicializa a chave secreta para assinatura de tokens JWT.
     */
    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Chave JWT não configurada ou vazia!");
        }

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("A chave JWT precisa ter pelo menos 64 bytes para HS512.");
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Gera um token JWT com os dados do usuário.
     *
     * @param user Usuário para quem o token será gerado.
     * @return Token JWT como {@code String}.
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole().getName())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.MINUTES)))
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Valida a integridade e expiração de um token JWT.
     *
     * @param token Token JWT a ser validado.
     * @throws JwtException se o token for inválido ou expirado.
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new JwtException("Token JWT inválido ou expirado.", e);
        }
    }

    /**
     * Extrai o ID do usuário a partir do token JWT.
     *
     * @param token Token JWT.
     * @return ID do usuário.
     */
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }

    /**
     * Extrai o nome de usuário (username) do token JWT.
     *
     * @param token Token JWT.
     * @return Nome de usuário.
     */
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Extrai os papéis (roles) do token JWT.
     *
     * @param token Token JWT.
     * @return Lista de autoridades do Spring Security.
     */
    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String role = claims.get("role", String.class);

        return List.of(new SimpleGrantedAuthority(String.format("ROLE_%s", role.toUpperCase())));
    }
}