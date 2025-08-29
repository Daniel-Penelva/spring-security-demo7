package com.api.spring_security_demo7.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.api.spring_security_demo7.user.User;

public class ApplicationAuditorAware implements AuditorAware<String>{

    /**
     * Obtém o auditor atual (usuário autenticado) para operações de auditoria.
     * @return um Optional contendo o ID do usuário autenticado ou vazio se não houver usuário autenticado.
     * 
    */

    @Override
    public Optional<String> getCurrentAuditor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  // obtém o contexto de autenticação atual

         // Verifica se o usuário está autenticado
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Retorna o ID do usuário autenticado 
        final User user = (User) authentication.getPrincipal();
        return Optional.ofNullable(user.getId());
    }
    
}

/**
 * ApplicationAuditorAware é uma implementação de AuditorAware que fornece o auditor atual para operações de auditoria.
 * 
 * AuditorAware é uma interface do Spring Data que permite capturar informações sobre o usuário que está realizando uma ação. Ou seja, quem criou ou modificou uma entidade.
 * Ele obtém o usuário autenticado a partir do contexto de segurança do Spring Security. 
 * 
 * O método getCurrentAuditor retorna um Optional contendo o ID do usuário autenticado ou vazio se não houver usuário autenticado. 
 * 
*/
