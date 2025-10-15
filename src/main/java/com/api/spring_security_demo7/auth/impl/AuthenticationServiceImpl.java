package com.api.spring_security_demo7.auth.impl;

import static com.api.spring_security_demo7.exception.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.api.spring_security_demo7.exception.ErrorCode.PASSWORD_MISMATCH;
import static com.api.spring_security_demo7.exception.ErrorCode.PHONE_ALREADY_EXISTS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.spring_security_demo7.auth.AuthenticationService;
import com.api.spring_security_demo7.auth.request.AuthenticationRequest;
import com.api.spring_security_demo7.auth.request.RefreshRequest;
import com.api.spring_security_demo7.auth.request.RegistrationRequest;
import com.api.spring_security_demo7.auth.response.AuthenticationResponse;
import com.api.spring_security_demo7.exception.BusinessException;
import com.api.spring_security_demo7.role.Role;
import com.api.spring_security_demo7.role.RoleRepository;
import com.api.spring_security_demo7.security.JwtService;
import com.api.spring_security_demo7.user.User;
import com.api.spring_security_demo7.user.UserMapper;
import com.api.spring_security_demo7.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    /**
     * Realiza o login do usuário, autentica suas credenciais e retorna tokens JWT válidos.
     *
     * @param request Objeto {@link AuthenticationRequest} contendo email e senha do usuário.
     * @return {@link AuthenticationResponse} contendo Access Token, Refresh Token e tipo do token.
     */
    @Override
    public AuthenticationResponse login(final AuthenticationRequest request) {

        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final User user = (User) auth.getPrincipal();
        final String token = this.jwtService.generateAccessToken(user.getUsername());
        final String refreshToken = this.jwtService.generateRefreshToken(user.getUsername());
        final String tokenType = "Bearer";

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build();
    }


    /**
     * Realiza o registro de um novo usuário no sistema.
     * Valida se o email e telefone já existem, verifica se as senhas batem,
     * associa a role {@code ROLE_USER} e persiste o usuário no banco.
     *
     * @param request Objeto {@link RegistrationRequest} contendo os dados do novo usuário.
     * @throws BusinessException Caso o email ou telefone já existam ou as senhas não coincidam.
     */
    @Override
    @Transactional
    public void register(final RegistrationRequest request) {

        checkUserEmail(request.getEmail());
        checkUserPhoneNumber(request.getPhoneNumber());
        checkPasswords(request.getPassword(), request.getConfirmPassword());

        final Role userRole = this.roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Role user does not exist"));
        final List<Role> roles = new ArrayList<>();
        roles.add(userRole);

        final User user = this.userMapper.toUser(request);
        user.setRoles(roles);
        log.debug("Saving user {}", user);
        this.userRepository.save(user);

        final List<User> users = new ArrayList<>();
        users.add(user);
        userRole.setUsers(users);

        this.roleRepository.save(userRole);
    }


    /**
     * Gera um novo Access Token baseado em um Refresh Token válido.
     *
     * @param req Objeto {@link RefreshRequest} contendo o refresh token.
     * @return {@link AuthenticationResponse} com o novo Access Token, refresh token e tipo.
     */
    @Override
    public AuthenticationResponse refreshToken(final RefreshRequest req) {

        final String newAccessToken = this.jwtService.refreshAccessToken(req.getRefreshToken());
        final String tokenType = "Bearer";
        
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(req.getRefreshToken())
                .tokenType(tokenType)
                .build();
    }


    /**
     * Verifica se o email informado já existe no banco.
     *
     * @param email Email a ser verificado.
     * @throws BusinessException Caso o email já exista.
     */
    private void checkUserEmail(final String email) {
        final boolean emailExists = this.userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            throw new BusinessException(EMAIL_ALREADY_EXISTS);
        }
    }


    /**
     * Verifica se a senha e a confirmação de senha coincidem. 
     *
     * @param password        Senha informada pelo usuário.
     * @param confirmPassword Confirmação da senha.
     * @throws BusinessException Caso as senhas não coincidam ou sejam nulas.
     */
    private void checkPasswords(final String password, final String confirmPassword) {
        if (password == null || !password.equals(confirmPassword)) {
            throw new BusinessException(PASSWORD_MISMATCH);
        }
    }


    /**
     * Verifica se o número de telefone informado já existe no banco.
     *
     * @param phoneNumber Número de telefone a ser verificado.
     * @throws BusinessException Caso o número de telefone já exista.
     */
    private void checkUserPhoneNumber(final String phoneNumber) {
        final boolean phoneNumberExists = this.userRepository.existsByPhoneNumber(phoneNumber);
        if (phoneNumberExists) {
            throw new BusinessException(PHONE_ALREADY_EXISTS);
        }
    }

}


/** ATENÇÃO!
 * Implementação do serviço de autenticação responsável por:
 * 
 *   Login de usuários, gerando Access Token e Refresh Token via {@link JwtService}.
 *   Registro de novos usuários, validando email, telefone e senha.
 *   Refresh de Access Token usando um Refresh Token válido.
 * 
 * Esta classe integra Spring Security, JWT e persistência de usuários e roles no banco.
 */
