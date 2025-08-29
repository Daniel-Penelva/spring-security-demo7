package com.api.spring_security_demo7.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailDomainValidator.class)
public @interface NonDisposableEmail {

    String message() default "Disposable email addresses are not allowed";  // Mensagem padrão de validação 

    Class<?>[] groups() default {};  // Grupos de validação (padrão vazio)

    Class<? extends Payload>[] payload() default {};  // Payload para informações adicionais (padrão vazio)
    
}

/**
 * NonDisposableEmail é uma anotação personalizada para validação de emails, garantindo que o email não pertença a um domínio descartável.
 * Ela utiliza o validador EmailDomainValidator para realizar a validação. 
 * A anotação pode ser aplicada a campos e parâmetros, e inclui propriedades para mensagem de erro, grupos de validação e payload. 
 * A interface Constraint faz parte do Jakarta Bean Validation (JSR 380) e é usada para definir anotações de validação personalizadas.
 * A anotação @Target especifica que ela pode ser aplicada a campos e parâmetros, enquanto @Retention define que a anotação estará disponível em tempo de execução.
 * A anotação @Constraint vincula a anotação ao validador EmailDomainValidator, que contém a lógica de validação real.
*/
