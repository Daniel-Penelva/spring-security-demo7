package com.api.spring_security_demo7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfig {

    /**
     * Bean para codificação de senhas usando BCryptPasswordEncoder.
     * @return uma instância de PasswordEncoder.
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Bean para o AuthenticationManager, que gerencia a autenticação dos usuários.
     * @param config a configuração de autenticação.
     * @return uma instância de AuthenticationManager.
     * @throws Exception se ocorrer um erro ao obter o AuthenticationManager.
    */
    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    /**
     * Bean para auditoria, que fornece o auditor atual (usuário autenticado).
     * @return uma instância de AuditorAware.
    */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditorAware();
    }
    
}

/**
 * BeansConfig é uma classe de configuração do Spring que define vários beans utilizados na aplicação.
 * Ela inclui beans para codificação de senhas, gerenciamento de autenticação e auditoria (usuário autenticado).
 * 
 * OBS. Esses beans são essenciais para a segurança e auditoria da aplicação.
*/
