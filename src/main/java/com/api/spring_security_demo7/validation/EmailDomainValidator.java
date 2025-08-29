package com.api.spring_security_demo7.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailDomainValidator implements ConstraintValidator<NonDisposableEmail, String> {


    private final Set<String> blocked;  // Conjunto de domínios de email bloqueados (descartáveis)


    /**
     * Construtor que inicializa o validador com a lista de domínios bloqueados.
     * @param domains a lista de domínios de email bloqueados, injetada a partir das propriedades da aplicação.
     * Este construtor utiliza a anotação @Value para injetar a lista de domínios de email descartáveis definida na configuração da aplicação.
     * Os domínios são convertidos para minúsculas e armazenados em um conjunto para facilitar a validação.
    */
    public EmailDomainValidator(@Value("${app.security.disposable-email}") final List<String> domains) {
            this.blocked = domains.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }


    /**
     * Valida se o email fornecido não pertence a um domínio bloqueado (descartável).
     * @param email o email a ser validado.
     * @param context o contexto de validação.
     * @return true se o email for válido (não descartável) ou null, false se for de um domínio bloqueado.
     * Se o email for null ou não contiver o caractere '@', a validação retorna true (considerando-o válido).
     * A validação extrai o domínio do email e verifica se ele está na lista de domínios bloqueados.
     * Se estiver, retorna false; caso contrário, retorna true.
    */
    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        
        // Se o email for null ou não contiver '@', considera válido (retorna true)
        if (email == null || !email.contains("@")) {
            return true; 
        }

        final int atIndex = email.lastIndexOf('@') + 1;  // Posição do caractere '@' no email
        final int dotIndex = email.lastIndexOf('.');      // Posição do último caractere '.' no email
        final String domain = email.substring(atIndex, dotIndex).toLowerCase(); // Extrai o domínio do email e converte para minúsculas
        return !this.blocked.contains(domain); // Verifica se o domínio está na lista de bloqueados
    }

}


/**
 * EmailDomainValidator é uma implementação de ConstraintValidator que valida se um email pertence a um domínio bloqueado (descartável).
 * Realiza a validação para a anotação personalizada NonDisposableEmail. 
 * 
 * Ele utiliza uma lista de domínios bloqueados injetada a partir das propriedades da aplicação.
 * 
 * A interface ConstraintValidator é parte do Jakarta Bean Validation (JSR 380) e permite a criação de validadores personalizados. 
 * 
 * O método isValid verifica se o email fornecido é válido, retornando false se o domínio for bloqueado e true caso contrário.
 * Se o email for null ou não contiver o caractere '@', a validação considera-o válido.
 * 
 * OBS. Essa validação é útil para impedir o uso de emails descartáveis em cadastros ou formulários.
 * 
*/
