package com.api.spring_security_demo7.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.spring_security_demo7.user.request.ChangePasswordRequest;
import com.api.spring_security_demo7.user.request.ProfileUpdateRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;


    /**
     * Atualiza as informações de perfil do usuário autenticado.
     * @param profileUpdateRequest Objeto contendo as novas informações de perfil.
     * @param authentication O objeto de autenticação que contém os detalhes do usuário autenticado.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado.
    */
    @PatchMapping("/me")
    @ResponseStatus(code =  HttpStatus.NO_CONTENT)
    public void updateProfile(@RequestBody @Valid final ProfileUpdateRequest profileUpdateRequest, final Authentication authentication) {
        this.userService.updateProfileInfo(profileUpdateRequest, getUserId(authentication));
    }


    /**
     * Altera a senha do usuário autenticado.
     * @param changePasswordRequest Objeto contendo a senha atual, nova senha e confirmação da nova senha.
     * @param authentication O objeto de autenticação que contém os detalhes do usuário autenticado.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado ou se a senha atual estiver incorreta.
    */
    @PostMapping("/me/password")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid final ChangePasswordRequest changePasswordRequest, final Authentication authentication) {
        this.userService.changedPassword(changePasswordRequest, getUserId(authentication));
    }


    /**
     * Desativa a conta do usuário autenticado.
     * @param authentication O objeto de autenticação que contém os detalhes do usuário autenticado.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado ou se a conta já estiver desativada.
     * Observação: Após a desativação, o usuário não poderá mais acessar sua conta até que ela seja reativada por um administrador.
     * A desativação da conta é uma ação permanente e deve ser realizada com cautela.
    */
    @PatchMapping("/me/deactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deactivatedAccount(final Authentication authentication) {
        this.userService.deactivatedAccount(getUserId(authentication));
    }


    /**
     * Reativa a conta do usuário autenticado.
     * @param authentication O objeto de autenticação que contém os detalhes do usuário autenticado.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado.
     * Observação: A reativação da conta permite que o usuário volte a acessar sua conta e todas as suas funcionalidades.
     * A reativação pode ser necessária se a conta foi desativada anteriormente por engano ou se o usuário deseja retomar o uso dos serviços. 
     * Após a reativação, o usuário poderá acessar sua conta normalmente.
    */
    @PatchMapping("/me/reactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void reactivatedAccount(final Authentication authentication) {
        this.userService.reactivatedAccount(getUserId(authentication));
    }


    /**
     * Exclui a conta do usuário autenticado.
     * @param authentication O objeto de autenticação que contém os detalhes do usuário autenticado.
     * @throws BusinessException Se o usuário com o ID fornecido não for encontrado.
     * Observação: A exclusão da conta é uma ação permanente e irreversível.
     * Após a exclusão, todos os dados associados à conta do usuário serão removidos do sistema.
     * Dica: Certifique-se de que o usuário realmente deseja excluir sua conta antes de prosseguir com esta ação.
    */
    @DeleteMapping("/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletedAccount(final Authentication authentication) {
        this.userService.deletedAccount(getUserId(authentication));
    }


    /**
     * Obtém o ID do usuário a partir do objeto de autenticação.
     * @param authentication O objeto de autenticação que contém os detalhes do usuário autenticado.
     * @return O ID do usuário autenticado.
     * @throws ClassCastException Se o principal no objeto de autenticação não for uma instância de User.
    */
    private String getUserId(final Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}

/**
 * Atenção!
 * Esta classe é o controlador REST para gerenciar operações relacionadas ao usuário.
 * Ela define endpoints para atualizar o perfil do usuário, alterar a senha, desativar, reativar e excluir a conta do usuário autenticado.
 * Utiliza a interface UserService para delegar a lógica de negócios e manipulação de dados.
*/
