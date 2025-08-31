package com.api.spring_security_demo7.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {

    /** OBS.
     * Códigos de erro comuns:
     * 400 BAD_REQUEST
     * 401 UNAUTHORIZED
     * 403 FORBIDDEN
     * 404 NOT_FOUND
     * 409 CONFLICT
     * 500 INTERNAL_SERVER_ERROR
    */

    /**
     * São definidos códigos de erro específicos para diferentes situações que podem ocorrer na aplicação.
     * Cada código de erro é acompanhado por uma mensagem padrão e um status HTTP apropriado.
     * Esses códigos de erro podem ser utilizados em exceções personalizadas para fornecer feedback claro e consistente aos usuários e desenvolvedores.
     * Exemplos de erros incluem: email já existente, senha incorreta, usuário não encontrado, entre outros.
    */
    
    EMAIL_ALREADY_EXISTS("ERR_EMAIL_EXISTS", "Email already exists", CONFLICT),
    PHONE_ALREADY_EXISTS("ERR_PHONE_EXISTS", "An account with this phone number already exists", CONFLICT),
    PASSWORD_MISMATCH("ERR_PASSWORD_MISMATCH", "The password and confirmation do not match", BAD_REQUEST),
    CHANGE_PASSWORD_MISMATCH("ERR_PASSWORD_MISMATCH", "New password and confirmation do not match", BAD_REQUEST),
    ERR_SENDING_ACTIVATION_EMAIL("ERR_SENDING_ACTIVATION_EMAIL",
                                 "An error occurred while sending the activation email",
                                 HttpStatus.INTERNAL_SERVER_ERROR),

    ERR_USER_DISABLED("ERR_USER_DISABLED",
                      "User account is disabled, please activate your account or contact the administrator",
                      UNAUTHORIZED),
    INVALID_CURRENT_PASSWORD("INVALID_CURRENT_PASSWORD", "The current password is incorrect", BAD_REQUEST),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found", NOT_FOUND),
    ACCOUNT_ALREADY_DEACTIVATED("ACCOUNT_ALREADY_DEACTIVATED", "Account has been deactivated", BAD_REQUEST),
    BAD_CREDENTIALS("BAD_CREDENTIALS", "Username and / or password is incorrect", UNAUTHORIZED),
    INTERNAL_EXCEPTION("INTERNAL_EXCEPTION",
                       "An internal exception occurred, please try again or contact the admin",
                       HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "Cannot find user with the provided username", NOT_FOUND),
    CATEGORY_ALREADY_EXISTS_FOR_USER("CATEGORY_ALREADY_EXISTS_FOR_USER", "Category already exists for this user", CONFLICT),
    ;


    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    ErrorCode(String code, String defaultMessage, HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
    
}

/** Atenção!
 * Este Enum é utilizado para definir códigos de erro, mensagens padrão e status HTTP associados a diferentes tipos de erros que podem ocorrer na aplicação.
 * Cada constante do enum representa um erro específico, facilitando a gestão e a padronização dos erros na aplicação.
 * 
 * As propriedades incluem:
 * - code: Um código único para identificar o erro.
 * - defaultMessage: Uma mensagem padrão que descreve o erro.
 * - status: O status HTTP associado ao erro, que pode ser usado para respostas de APIs.
 * 
 * Exemplos de erros incluem: email já existente, senha incorreta, usuário não encontrado, entre outros.
 * 
 * Esses códigos de erro podem ser utilizados em exceções personalizadas para fornecer feedback claro e consistente aos usuários e desenvolvedores.
*/
