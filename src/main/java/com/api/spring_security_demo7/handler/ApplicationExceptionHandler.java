package com.api.spring_security_demo7.handler;

import static com.api.spring_security_demo7.exception.ErrorCode.BAD_CREDENTIALS;
import static com.api.spring_security_demo7.exception.ErrorCode.ERR_USER_DISABLED;
import static com.api.spring_security_demo7.exception.ErrorCode.INTERNAL_EXCEPTION;
import static com.api.spring_security_demo7.exception.ErrorCode.USERNAME_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api.spring_security_demo7.exception.BusinessException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

        /**
         * Manipula exceções de negócio lançadas na aplicação.
         * 
         * @param ex A exceção de negócio capturada.
         * @return Uma resposta HTTP contendo os detalhes do erro.
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusiness(final BusinessException ex) {
                final ErrorResponse body = ErrorResponse.builder()
                                .code(ex.getErrorCode().getCode())
                                .message(ex.getMessage())
                                .build();

                log.error("BusinessException: {}", body);
                log.debug(ex.getMessage(), ex);
                return ResponseEntity
                                .status(ex.getErrorCode().getStatus() != null ? ex.getErrorCode().getStatus()
                                                : BAD_REQUEST)
                                .body(body);

        }

        /**
         * Manipula exceções lançadas quando uma conta de usuário está desativada.
         * 
         * @return Uma resposta HTTP indicando que a conta está desativada.
         */
        @ExceptionHandler(DisabledException.class)
        public ResponseEntity<ErrorResponse> handleBusiness() {
                final ErrorResponse body = ErrorResponse.builder()
                                .code(ERR_USER_DISABLED.getCode())
                                .message(ERR_USER_DISABLED.getDefaultMessage())
                                .build();
                return ResponseEntity.status(UNAUTHORIZED)
                                .body(body);
        }

        /**
         * O método abaixo lida com exceções lançadas quando a validação dos argumentos do método falha.
         * 
         * @param exp A exceção capturada que contém detalhes sobre os erros de validação.
         * @return Uma resposta HTTP contendo os detalhes dos erros de validação.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exp) {
                final List<ErrorResponse.ValidationError> errors = new ArrayList<>();
                exp.getBindingResult()
                                .getAllErrors()
                                .forEach(error -> {
                                        final String fieldName = ((FieldError) error).getField();
                                        final String errorCode = error.getDefaultMessage();
                                        errors.add(ErrorResponse.ValidationError.builder()
                                                        .field(fieldName)
                                                        .code(errorCode)
                                                        .message(errorCode)
                                                        .build());
                                });
                final ErrorResponse errorResponse = ErrorResponse.builder()
                                .validationErrors(errors)
                                .build();
                return new ResponseEntity<>(errorResponse, BAD_REQUEST);
        }

        /**
         * Manipula exceções lançadas quando as credenciais fornecidas são inválidas.
         * 
         * @param exception A exceção capturada que indica credenciais inválidas.
         * @return Uma resposta HTTP indicando que as credenciais são inválidas.
         * 
         */
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException exception) {
                log.debug(exception.getMessage(), exception);
                final ErrorResponse response = ErrorResponse.builder()
                                .message(BAD_CREDENTIALS.getDefaultMessage())
                                .code(BAD_CREDENTIALS.getCode())
                                .build();
                return new ResponseEntity<>(response, UNAUTHORIZED);
        }

        /**
         * Manipula exceções lançadas quando uma entidade não é encontrada no banco de dados.
         * 
         * @param exception A exceção capturada que indica que a entidade não foi encontrada.
         * @return Uma resposta HTTP indicando que a entidade não foi encontrada.
         */
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleException(final EntityNotFoundException exception) {
                log.debug(exception.getMessage(), exception);
                final ErrorResponse errorResponse = ErrorResponse.builder()
                                .code("TBD")
                                .message(exception.getMessage())
                                .build();
                return new ResponseEntity<>(errorResponse, NOT_FOUND);
        }

        /**
         * Manipula exceções lançadas quando um nome de usuário não é encontrado.
         * 
         * @param exception A exceção capturada que indica que o nome de usuário não foi encontrado.
         * 
         * @return Uma resposta HTTP indicando que o nome de usuário não foi encontrado.
         */
        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException exception) {
                log.debug(exception.getMessage(), exception);
                final ErrorResponse response = ErrorResponse.builder()
                                .code(USERNAME_NOT_FOUND.getCode())
                                .message(USERNAME_NOT_FOUND.getDefaultMessage())
                                .build();
                return new ResponseEntity<>(response,
                                NOT_FOUND);
        }

        /**
         * Manipula exceções lançadas quando uma operação é negada devido a falta de autorização.
         * 
         * @param exception A exceção capturada que indica que a operação foi negada.
         * @return Uma resposta HTTP indicando que a operação foi negada.
         */
        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ErrorResponse> handleException(final AuthorizationDeniedException exception) {
                log.debug(exception.getMessage(), exception);
                final ErrorResponse response = ErrorResponse.builder()
                                .message("You are not authorized to perform this operation")
                                .build();
                return new ResponseEntity<>(response, UNAUTHORIZED);
        }


        /**
         * Manipula todas as outras exceções não tratadas que podem ocorrer na
         * aplicação.
         * 
         * @param exception A exceção capturada.
         * @return Uma resposta HTTP indicando que ocorreu um erro interno no servidor.
        */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
                log.error(exception.getMessage(), exception);
                final ErrorResponse response = ErrorResponse.builder()
                                .code(INTERNAL_EXCEPTION.getCode())
                                .message(INTERNAL_EXCEPTION.getDefaultMessage())
                                .build();
                return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
        }

}

/**
 * Atenção!
 * Esta classe é um manipulador global de exceções para uma aplicação Spring Boot.
 * Ela captura várias exceções que podem ser lançadas durante a execução da aplicação e retorna respostas HTTP apropriadas com detalhes sobre os erros.
 * Cada método é anotado com @ExceptionHandler para especificar o tipo de exceção que ele manipula.
 * As respostas incluem códigos de erro, mensagens e, em alguns casos, detalhes de validação.
 * Isso ajuda a padronizar o tratamento de erros e a fornecer feedback claro aos clientes da API.
 * As exceções tratadas incluem:
 * - BusinessException: Exceções de negócio personalizadas.
 * - DisabledException: Conta de usuário desativada.
 * - MethodArgumentNotValidException: Erros de validação de argumentos do método.
 * - BadCredentialsException: Credenciais inválidas.
 * - EntityNotFoundException: Entidade não encontrada no banco de dados.
 * - UsernameNotFoundException: Nome de usuário não encontrado.
 * - AuthorizationDeniedException: Operação negada por falta de autorização.
 * - Exception: Qualquer outra exceção não tratada.
 * 
 * Cada método registra o erro e constrói uma resposta adequada para o cliente.
*/