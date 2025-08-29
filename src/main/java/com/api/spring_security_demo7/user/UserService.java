package com.api.spring_security_demo7.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.api.spring_security_demo7.user.request.ChangePasswordRequest;
import com.api.spring_security_demo7.user.request.ProfileUpdateRequest;

public interface UserService extends UserDetailsService{

    void updateProfileInfo(ProfileUpdateRequest profileUpdateRequest, String userId);

    void changedPassword(ChangePasswordRequest changePasswordRequest, String userId);

    void deactivatedAccount(String userId);

    void reactivatedAccount(String userId);

    void deletedAccount(String userId);
    
}

/** UserDetailsService
 * Implementando a interface UserDetails para fornecer informações de autenticação e autorização sobre o usuário.
 * OBS. Implementa o método loadUserByUsername para carregar os detalhes do usuário com base no nome de usuário.
 * 
 * Este serviço de usuário define várias operações relacionadas à gestão do perfil e da conta do usuário. 
 * 
 * Métodos:
 * - updateProfileInfo: Atualiza as informações do perfil do usuário com base nos dados fornecidos no ProfileUpdateRequest.
 * - changedPassword: Altera a senha do usuário com base nos dados fornecidos no ChangePasswordRequest.
 * - deactivatedAccount: Desativa a conta do usuário com o ID fornecido.
 * - reactivatedAccount: Reativa a conta do usuário com o ID fornecido.
 * - deletedAccount: Exclui a conta do usuário com o ID fornecido.
*/
