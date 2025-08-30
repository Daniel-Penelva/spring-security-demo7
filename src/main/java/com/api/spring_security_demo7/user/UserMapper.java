package com.api.spring_security_demo7.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.spring_security_demo7.auth.request.RegistrationRequest;
import com.api.spring_security_demo7.user.request.ProfileUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    /*
     * Mapeia um RegistrationRequest para um User entity - aqui converte os campos do request para os campos da entidade User.
     * As senhas sao codificadas antes de serem armazenadas. 
     * Retorna a entidade User criada.
    */
    public User toUser(final RegistrationRequest request) {
        return User.builder()
            .firstName(request.getFirstname())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .password(this.passwordEncoder.encode(request.getPassword()))
            .enabled(true)
            .locked(false)
            .credentialsExpired(false)
            .emailVerified(false)
            .phoneVerified(false)
            .build();
    }


    /**
     * Atualiza as informacoes do usuario com base nos dados fornecidos na ProfileUpdateRequest.
     * Verifica se cada campo no request é diferente do valor atual no usuario e se nao é nulo ou vazio (para strings) antes de atualizar.
     * Isso evita atualizacoes desnecessarias e garante que apenas os campos modificados sejam alterados. 
     * @param user A entidade User a ser atualizada.
     * @param request O ProfileUpdateRequest contendo os novos dados do usuario.
    */
    public void mergeUserInfo(final User user, final ProfileUpdateRequest request) {

        if (StringUtils.isNotBlank(request.getFirstName()) && !user.getFirstName().equals(request.getFirstName())) { // Atualiza o primeiro nome se for diferente e nao nulo ou vazio 
            user.setFirstName(request.getFirstName());
        }
        if(StringUtils.isNotBlank(request.getLastName()) && !user.getLastName().equals(request.getLastName())) { // Atualiza o sobrenome se for diferente e nao nulo ou vazio 
            user.setLastName(request.getLastName());
        }
        if(request.getDateOfBirth() != null && !request.getDateOfBirth().equals(user.getDateOfBirth())) { // Atualiza a data de nascimento se for diferente e nao nulo
            user.setDateOfBirth(request.getDateOfBirth());
        }
    }
    
}

/**
 * OBS.
 * StringUtils.isNotBlank() é um método da biblioteca Apache Commons Lang que verifica se uma string não é nula, não está vazia e não contém apenas espaços em branco.
 * Ele é útil para validar entradas de texto antes de realizar operações que exigem uma string válida.
*/

/** Atenção!
 * Este mapper é responsável por converter entre diferentes representações de dados relacionadas ao usuário.
 * Ele inclui métodos para mapear um RegistrationRequest para uma entidade User e para mesclar informações de um ProfileUpdateRequest em uma 
 * entidade User existente.
 * A codificação de senhas é tratada usando um PasswordEncoder para garantir a segurança dos dados do usuário.
*/
