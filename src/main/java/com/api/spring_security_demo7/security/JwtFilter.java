package com.api.spring_security_demo7.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    /**
     * Intercepta cada requisição HTTP e realiza a validação do JWT presente no header "Authorization".
     * 
     * O método ignora endpoints que não exigem autenticação, extrai o token JWT,
     * valida-o utilizando {@link JwtService}, e caso seja válido, cria e define
     * a autenticação no {@link SecurityContextHolder} para que o Spring Security
     * reconheça o usuário.
     *
     * @param request     Objeto {@link HttpServletRequest} representando a requisição HTTP.
     * @param response    Objeto {@link HttpServletResponse} representando a resposta HTTP.
     * @param filterChain Objeto {@link FilterChain} que permite que a requisição continue para
     *                    o próximo filtro ou controller.
     * @throws ServletException Caso ocorra algum erro durante o processamento do filtro.
     * @throws IOException      Caso ocorra algum erro de I/O durante a leitura do header ou da requisição.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Ignora endpoints de autenticação - Rotas como /api/v1/auth/login ou /register não precisam de token, então o filtro passa adiante sem validação.
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);  // Lê o header Authorization
        final String jwt;
        final String username;

        // Se o usuário existe no token e ainda não está autenticado no contexto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = this.jwtService.extractUsername(jwt);  // Extrai usuário do token. Se não houver usuário ou token inválido, não faz autenticação.

        // Valida token e adiciona autenticação no SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Cria um UsernamePasswordAuthenticationToken e adiciona ao SecurityContextHolder. Isso permite que o Spring Security reconheça o usuário para autorização nos endpoints. 
            if (this.jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // continua o fluxo da requisição
        filterChain.doFilter(request, response);

    }
}

/** ATENÇÃO!
 * Filtro de autenticação JWT que intercepta todas as requisições HTTP
 * e valida tokens presentes no header "Authorization".
 * 
 * Este filtro realiza os seguintes passos:
 * 
 *   -> Ignora endpoints de autenticação (ex: "/api/v1/auth") para não exigir token.
 *   -> Extrai o token JWT do header "Authorization" no formato "Bearer &lt;token&gt;".
 *   -> Extrai o username do token e carrega os detalhes do usuário utilizando {@link UserDetailsService}.
 *   -> Valida se o token é válido e, se for, adiciona a autenticação no {@link SecurityContextHolder}.
 *   -> Permite que a requisição prossiga para o próximo filtro ou controller.</li>
 * 
 * Esta implementação integra JWT com Spring Security, garantindo que apenas usuários
 * com tokens válidos possam acessar endpoints protegidos da aplicação.
 */