package com.api.spring_security_demo7.user.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.spring_security_demo7.exception.BusinessException;
import com.api.spring_security_demo7.exception.ErrorCode;
import com.api.spring_security_demo7.user.User;
import com.api.spring_security_demo7.user.UserMapper;
import com.api.spring_security_demo7.user.UserRepository;
import com.api.spring_security_demo7.user.UserService;
import com.api.spring_security_demo7.user.request.ChangePasswordRequest;
import com.api.spring_security_demo7.user.request.ProfileUpdateRequest;

import static com.api.spring_security_demo7.exception.ErrorCode.CHANGE_PASSWORD_MISMATCH;
import static com.api.spring_security_demo7.exception.ErrorCode.INVALID_CURRENT_PASSWORD;
import static com.api.spring_security_demo7.exception.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;;
    private final UserMapper userMapper;

    /**
     * Carrega os detalhes do usuário com base no email fornecido.
     * @param userEmail O email do usuário a ser carregado.
     * @return Os detalhes do usuário.
     * @throws UsernameNotFoundException Se o usuário com o email fornecido não for encontrado
    */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userEmail));
    }


    /**
     * Atualiza as informações de perfil do usuário.
     * @param profileUpdateRequest Objeto contendo as novas informações de perfil.
     * @param userId O ID do usuário cujo perfil será atualizado.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado.
    */
    @Override
    public void updateProfileInfo(final ProfileUpdateRequest profileUpdateRequest, final String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        this.userMapper.mergeUserInfo(savedUser, profileUpdateRequest);
        this.userRepository.save(savedUser);
    }


    /**
     * Altera a senha do usuário.
     * @param changePasswordRequest Objeto contendo a senha atual, nova senha e confirmação da nova senha.
     * @param userId O ID do usuário cuja senha será alterada.
     * @throws BusinessException Se as senhas não coincidirem, se a senha atual estiver incorreta ou se o usuário com o ID fornecido não for encontrado.
    */
    @Override
    public void changedPassword(final ChangePasswordRequest changePasswordRequest, final String userId) {
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            throw new BusinessException(CHANGE_PASSWORD_MISMATCH, "New password and confirm new password do not match");  // lança exceção se as senhas nao coincidirem 
        }

        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));  // busca o usuario pelo ID, se nao encontrar lança exceção

        if (!this.passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), savedUser.getPassword())) {
            throw new BusinessException(INVALID_CURRENT_PASSWORD, "Current password is incorrect"); // lança exceção se a senha atual estiver incorreta
        }

        final String encoded = this.passwordEncoder.encode(changePasswordRequest.getNewPassword()); // codifica a nova senha 
        savedUser.setPassword(encoded); // atualiza a senha do usuario com a nova senha codificada
        this.userRepository.save(savedUser); // salva as alterações no repositório
    }


    /**
     * Desativa a conta do usuário.
     * @param userId O ID do usuário cuja conta será desativada.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado ou se a conta já estiver desativada.
    */
    @Override
    public void deactivatedAccount(final String userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));  // busca o usuario pelo ID, se nao encontrar lança exceção

        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED); // lança exceção se a conta ja estiver desativada. "!user.isEnabled()" verifica se a conta está ativa.
        }

        user.setEnabled(false); // desativa a conta do usuario
        this.userRepository.save(user); // salva as alterações no repositório
    }



    /*
     * Reativa a conta do usuário.
     * @param userId O ID do usuário cuja conta será reativada.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado ou se a conta já estiver reativada.
    */
    @Override
    public void reactivatedAccount(final String userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));  // busca o usuario pelo ID, se nao encontrar lança exceção

        if (user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED); // lança exceção se a conta ja estiver reativada. "user.isEnabled()" verifica se a conta está ativa.
        }

        user.setEnabled(true); // reativa a conta do usuario
        this.userRepository.save(user); // salva as alterações no repositório
    }

    @Override
    public void deletedAccount(String userId) {
        // este método precisa do restante das entidades
        // a lógica é apenas agendar um perfil para exclusão
        // e então uma tarefa agendada pegará os perfis e excluirá tudo
    }

}

/**
 * Atenção!
 * Esta classe é a implementação do serviço de usuário, responsável por gerenciar operações relacionadas aos usuários.
 * Ela implementa a interface UserService e fornece funcionalidades como carregar detalhes do usuário, atualizar informações de perfil, 
 * alterar senha, desativar e reativar contas.
 * A classe utiliza um UserRepository para interagir com o banco de dados, um PasswordEncoder para manipulação segura de senhas,
 * e um UserMapper para mapear dados entre diferentes representações.
 * Cada método é projetado para lidar com casos específicos, lançando exceções de negócio quando necessário para garantir a integridade dos dados e a segurança.
 * A anotação @Service indica que esta classe é um componente de serviço gerenciado pelo Spring, e @RequiredArgsConstructor é usada para injeção automática de dependências.
 * A anotação @Transactional é usada para gerenciar transações de banco de dados, garantindo que as operações sejam executadas de forma atômica.
 * Esta implementação é crucial para a gestão de usuários em uma aplicação, especialmente em contextos que envolvem autenticação e autorização.
 * 
*/
