package com.api.spring_security_demo7.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    public static final String TOKEN_TYPE = "token_type";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    /**
     * Construtor responsável por carregar as chaves RSA a partir de arquivos PEM
     * armazenados em {@code resources/keys/local-only}.
     *
     * @throws Exception Caso os arquivos não sejam encontrados, estejam em formato inválido
     *                   ou ocorra falha na geração das chaves.
     */
    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("keys/local-only/private_key.pem");
        this.publicKey = KeyUtils.loadPublicKey("keys/local-only/public_key.pem");
    }


    /**
     * Gera um token JWT de acesso (Access Token) para o usuário informado.
     * Este token possui o tipo {@code ACCESS_TOKEN} e expira conforme
     * a configuração {@code app.security.jwt.access-token-expiration}.
     *
     * @param username Nome de usuário que será definido como o "subject" do token.
     * @return String contendo o JWT assinado.
     */
    public String generateAccessToken(final String username) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "ACCESS_TOKEN");
        return buildToken(username, claims, this.accessTokenExpiration);
    }


    /**
     * Gera um token JWT de atualização (Refresh Token) para o usuário informado.
     * Este token possui o tipo {@code REFRESH_TOKEN} e expira conforme
     * a configuração {@code app.security.jwt.refresh-token-expiration}.
     *
     * @param username Nome de usuário que será definido como o "subject" do token.
     * @return String contendo o JWT assinado.
     */
    public String generateRefreshToken(final String username) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "REFRESH_TOKEN");
        return buildToken(username, claims, this.refreshTokenExpiration);
    }


    /**
     * Constrói e assina um JWT com base nas informações fornecidas.
     *
     * @param username    Nome de usuário que será definido como "subject" do token.
     * @param claims      Claims adicionais que serão inseridas no token.
     * @param expiration  Tempo de expiração em milissegundos a partir do momento atual.
     * @return String contendo o JWT assinado.
     */
    public String buildToken(final String username, final Map<String, Object> claims, final long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(this.privateKey)
                .compact();
    }


    /**
     * Verifica se um token é válido para um usuário esperado.
     * Um token é considerado válido se:
     *   O "subject" do token corresponde ao usuário esperado.
     *   O token não está expirado.
     *
     * @param token            O JWT a ser validado.
     * @param expectedUsername Nome de usuário esperado (comparado com o "subject" do token).
     * @return {@code true} se o token for válido, caso contrário {@code false}.
     */
    public boolean isTokenValid(final String token, final String expectedUsername) {
        final String username = extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }


    /**
     * Extrai o nome de usuário (subject) de um token JWT.
     *
     * @param token JWT do qual será extraído o subject.
     * @return String contendo o nome de usuário.
     */
    public String extractUsername(final String token) {
        return extractClaims(token).getSubject();
    }


    /**
     * Verifica se um token já está expirado.
     *
     * @param token JWT a ser verificado.
     * @return {@code true} se o token estiver expirado, caso contrário {@code false}.
     */
    private boolean isTokenExpired(final String token) {
        return extractClaims(token).getExpiration()
                .before(new Date());
    }


    /**
     * Extrai todas as claims de um token JWT, validando-o com a chave pública.
     *
     * @param token JWT do qual as claims serão extraídas.
     * @return Objeto {@link Claims} contendo as informações do token.
     * @throws RuntimeException Caso o token seja inválido ou não possa ser validado.
     */
    private Claims extractClaims(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (final JwtException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }


    /**
     * Gera um novo Access Token com base em um Refresh Token válido.
     * O método valida se o token informado:
     *   É do tipo {@code REFRESH_TOKEN}.
     *   Ainda não está expirado.
     * Caso seja válido, um novo Access Token é retornado.
     *
     * @param refreshToken Refresh Token previamente emitido.
     * @return Novo Access Token válido para o usuário.
     * @throws RuntimeException Caso o token não seja do tipo esperado ou esteja expirado.
     */
    public String refreshAccessToken(final String refreshToken) {
        final Claims claims = extractClaims(refreshToken);

        if (!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE, String.class))) {
            throw new RuntimeException("Invalid token type");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new RuntimeException("Refresh token expired");
        }

        final String username = claims.getSubject();
        return generateAccessToken(username);
    }

}

/* Atenção!
Essa classe é um serviço central de autenticação JWT (ou seja, gerencia o ciclo de vida de tokens JWT em uma aplicação) que: 
 * Carrega as chaves pública e privada RSA da pasta resources.
 * Gera tokens JWT de dois tipos:
 *      Access Token: usado para autenticar requisições no dia a dia.
 *      Refresh Token: usado para renovar o Access Token quando expira.
 *  Valida tokens (usuário correto, validade e assinatura).
 *  Extrai informações (claims) como o nome de usuário do token.
 * Renova tokens com base em um Refresh Token válido.
 * 
 * As chaves pública e privada são carregadas a partir da pasta {@code resources},
 * permitindo que o sistema use criptografia assimétrica para assinar e validar tokens.
 * 
 * Esta implementação pode ser considerada um padrão (boilerplate) em aplicações que utilizam
 * JWT com Spring Security. Normalmente, apenas detalhes como tempos de expiração,
 * claims adicionais e o algoritmo de assinatura variam entre projetos.
*/
