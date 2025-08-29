package com.api.spring_security_demo7.user.request;

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
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

}

/**
 * Este DTO request é usado para encapsular os dados necessários para alterar a senha de um usuário.
 * Ele contém os seguintes campos: currentPassword (senha atual), newPassword (nova senha) e confirmNewPassword (confirmação da nova senha).
*/