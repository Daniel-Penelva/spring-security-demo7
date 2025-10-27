package com.api.spring_security_demo7.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Lista de endpoints públicos que não requerem autenticação.
     * Inclui rotas de autenticação e documentação Swagger/OpenAPI.
     */
    private static final String[] PUBLIC_URLS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtFilter jwtFilter;  /** Filtro JWT responsável por validar os tokens em cada requisição. */

     /**
     * Define a cadeia de filtros de segurança (Security Filter Chain) da aplicação.
     * Este método configura:
     * Desabilitação do CSRF (não necessário em APIs stateless)
     *   . Permissão de acesso público às rotas listadas em {@link #PUBLIC_URLS}
     *   . Exigência de autenticação para qualquer outra requisição
     *   . Criação de sessão desabilitada ({@code STATELESS})
     *   . Adição do {@link JwtFilter} antes do filtro padrão de autenticação
     * 
     * @param http Objeto {@link HttpSecurity} usado para configurar as regras de segurança.
     * @return Instância configurada de {@link SecurityFilterChain}.
     * @throws Exception Caso ocorra erro durante a configuração da segurança.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(STATELESS))
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}

/**
 * ATENÇÃO!
 * Classe de configuração principal do Spring Security.
 *
 * Define as políticas de segurança da aplicação, incluindo:
 * . Rotas públicas e protegidas
 * . Política de criação de sessão
 * . Integração do filtro JWT ({@link JwtFilter})
 * 
 * As anotações {@link EnableWebSecurity} e {@link EnableMethodSecurity}
 * habilitam a segurança tanto em nível de requisição quanto em métodos.
 */
