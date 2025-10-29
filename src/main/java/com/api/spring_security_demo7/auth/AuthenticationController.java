package com.api.spring_security_demo7.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.spring_security_demo7.auth.request.AuthenticationRequest;
import com.api.spring_security_demo7.auth.request.RefreshRequest;
import com.api.spring_security_demo7.auth.request.RegistrationRequest;
import com.api.spring_security_demo7.auth.response.AuthenticationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {

    // Utilizando Swagger: http://localhost:8080/swagger-ui.html

    private final AuthenticationService authenticationService;

    /**
     * Endpoint responsável por autenticar um usuário com base em suas credenciais
     * (email e senha) e retornar os tokens JWT.
     *
     * @param request Objeto {@link AuthenticationRequest} contendo as credenciais
     *                do usuário.
     * @return {@link ResponseEntity} com {@link AuthenticationResponse} contendo
     *         o Access Token, Refresh Token e tipo do token.
     *
     * @see AuthenticationService#login(AuthenticationRequest)
     */
    @Operation(summary = "User Login", description = "Authenticate user and return JWT TOKEN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody final AuthenticationRequest request) {
        return ResponseEntity.ok(this.authenticationService.login(request));
    }

    /**
     * Endpoint responsável por registrar um novo usuário no sistema.
     *
     * Os dados enviados são validados e, em caso de sucesso, o usuário é persistido
     * no banco de dados com a role padrão {@code ROLE_USER}.
     *
     * @param request Objeto {@link RegistrationRequest} contendo as informações do
     *                novo usuário.
     * @return {@link ResponseEntity} com status {@code 201 CREATED} em caso de
     *         sucesso.
     *
     * @see AuthenticationService#register(RegistrationRequest)
     */
    @Operation(summary = "User Registration", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody final RegistrationRequest request) {
        this.authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Endpoint responsável por gerar um novo Access Token a partir de um Refresh Token válido.
     *
     * @param request Objeto {@link RefreshRequest} contendo o refresh token.
     * @return {@link ResponseEntity} com {@link AuthenticationResponse} contendo o
     *         novo Access Token.
     *
     * @see AuthenticationService#refreshToken(RefreshRequest)
     */
    @Operation(summary = "Refresh JWT Token", description = "Refresh the JWT token using a refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successully refreshed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody final RefreshRequest request) {
        return ResponseEntity.ok(this.authenticationService.refreshToken(request));
    }

}

/**
 * Atenção!!!
 * Controlador responsável por gerenciar as operações de autenticação da aplicação.
 * 
 * Este controller expõe endpoints para login, registro e atualização de tokens JWT,
 * delegando a lógica principal para {@link AuthenticationService}.
 * 
 * Anotações Swagger/OpenAPI são utilizadas para documentação automática dos endpoints.
 */
