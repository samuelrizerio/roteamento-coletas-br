package br.com.roteamento.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * SERVIÇO JWT
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Geração segura de tokens
 * - Validação de tokens
 * - Extração de claims
 * - Configuração de expiração
 * - Chave secreta configurável
 */
@Slf4j
@Service
public class JwtService {

    @Value("${spring.security.jwt.secret:}")
    private String secretKey;

    @Value("${spring.security.jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${spring.security.jwt.refresh-token.expiration:604800000}")
    private long refreshExpiration;

    private Key signingKey;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            log.error("JWT_SECRET não está configurado! A aplicação não funcionará corretamente.");
            throw new IllegalStateException("JWT_SECRET deve ser configurado como variável de ambiente ou propriedade");
        }
        this.signingKey = getSignInKey();
        log.info("JWT Service inicializado com sucesso");
    }

    /**
     * EXTRAIR EMAIL DO TOKEN
     * 
     * MELHORIAS:
     * - Extração segura de claims
     * - Tratamento de exceções
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * EXTRAIR CLAIM ESPECÍFICO
     * 
     * MELHORIAS:
     * - Função genérica para extração
     * - Reutilização de código
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * GERAR TOKEN PARA USUÁRIO
     * 
     * MELHORIAS:
     * - Claims customizados
     * - Configuração de expiração
     * - Assinatura segura
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * GERAR TOKEN COM CLAIMS EXTRAS
     * 
     * MELHORIAS:
     * - Claims adicionais
     * - Informações do usuário
     * - Timestamps
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * GERAR REFRESH TOKEN
     * 
     * MELHORIAS:
     * - Token de longa duração
     * - Renovação de acesso
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * CONSTRUIR TOKEN
     * 
     * MELHORIAS:
     * - Configuração flexível
     * - Claims padronizados
     * - Assinatura segura
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * VALIDAR TOKEN
     * 
     * MELHORIAS:
     * - Verificação de expiração
     * - Validação de usuário
     * - Tratamento de exceções
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * VERIFICAR SE TOKEN EXPIROU
     * 
     * MELHORIAS:
     * - Verificação de data
     * - Comparação segura
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * EXTRAIR DATA DE EXPIRAÇÃO
     * 
     * MELHORIAS:
     * - Extração de claim específico
     * - Tratamento de nulos
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * EXTRAIR TODOS OS CLAIMS
     * 
     * MELHORIAS:
     * - Parsing seguro do token
     * - Validação de assinatura
     * - Tratamento de exceções
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Erro ao extrair claims do token JWT", e);
            throw new RuntimeException("Token JWT inválido", e);
        }
    }

    /**
     * OBTER CHAVE DE ASSINATURA
     * 
     * MELHORIAS:
     * - Chave segura
     * - Suporta BASE64 e texto plano
     * - Validação de tamanho mínimo
     * - Algoritmo HMAC-SHA256
     */
    private Key getSignInKey() {
        byte[] keyBytes;
        
        try {
            // Tentar decodificar como BASE64 primeiro
            keyBytes = Decoders.BASE64.decode(secretKey);
            log.debug("JWT secret decodificado como BASE64");
        } catch (Exception e) {
            // Se falhar, tratar como texto plano e converter para bytes
            log.debug("JWT secret tratado como texto plano, convertendo para bytes");
            keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        }
        
        // Validar tamanho mínimo (pelo menos 32 bytes para HS256)
        if (keyBytes.length < 32) {
            log.warn("JWT secret muito curto ({} bytes). Recomendado: pelo menos 32 bytes. Gerando hash SHA-256.", keyBytes.length);
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(keyBytes);
            } catch (Exception e) {
                log.error("Erro ao gerar hash SHA-256 para JWT secret", e);
                throw new IllegalStateException("Não foi possível processar a chave JWT", e);
            }
        }
        
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
