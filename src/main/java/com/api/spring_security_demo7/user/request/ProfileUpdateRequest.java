package com.api.spring_security_demo7.user.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileUpdateRequest {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

}

/**
 * Este DTO request é usado para encapsular os dados necessários para atualizar o perfil de um usuário.
 * Ele contém os seguintes campos: firstName (primeiro nome), lastName (sobrenome) e dateOfBirth (data de nascimento).
*/
