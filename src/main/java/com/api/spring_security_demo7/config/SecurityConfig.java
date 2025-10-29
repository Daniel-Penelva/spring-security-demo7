package com.api.spring_security_demo7.config;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Este método configura usuários na memória para autenticação. Esta configuração é útil para testes ou aplicações simples.
    // Está definido três usuários: "user1" e "user2" com a role "USER", e "admin" com as roles "USER" e "ADMIN".
    //@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user1")
                .password("{noop}1234")
                .authorities("USER")
                .roles("USER")
                .build(),

            User.withUsername("user2")
                .password("{noop}1234")
                .authorities("USER")
                .roles("USER")
                .build(),
            
            User.withUsername("admin")
                .password("{noop}1234")
                .authorities("USER","ADMIN")
                .roles("ADMIN")
                .build()
        );
    }


    // Este método configura a cadeia de filtros de segurança para a aplicação.
    // Ele desabilita o CSRF, define regras de autorização para diferentes endpoints,
    // configura a política de criação de sessão como stateless e habilita a autenticação HTTP básica.
    //@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/test/public").permitAll()
                .requestMatchers("/api/test/private").authenticated()
                .requestMatchers("/api/test/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .build();
    }
}

// Esta Class SecurityConfig é responsável por configurar a segurança da aplicação Spring Boot usando Spring Security. 
*/