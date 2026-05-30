package br.com.phmiranda.comunidade.service;

import br.com.phmiranda.comunidade.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthTokenService {

    @Value("${comunidade.jwt.expiration}")
    private String expiracao;

    @Value("${comunidade.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Date dataGeracaoToken = new Date();
        Date dataExpiracaoToken = new Date(dataGeracaoToken.getTime() + Long.parseLong(expiracao));

        return Jwts.builder()
            .setIssuer("API da Comunidade")
            .setSubject(usuario.getId().toString())
            .setIssuedAt(dataGeracaoToken)
            .setExpiration(dataExpiracaoToken)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public Long getUsuarioId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }
}
