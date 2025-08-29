package com.api.spring_security_demo7.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
    
}

/**
 * Essa classe configura o JPA para habilitar a auditoria (usuário autenticado), permitindo o rastreamento de quem criou ou modificou entidades.
 * A anotação @EnableJpaAuditing ativa o suporte à auditoria do JPA, e o atributo auditorAwareRef especifica o bean que fornece o auditor atual.
*/
