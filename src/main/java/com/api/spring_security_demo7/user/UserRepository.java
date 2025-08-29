package com.api.spring_security_demo7.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}

/** JpaRepository
 * Extende a interface JpaRepository para fornecer operações CRUD e funcionalidades adicionais para a entidade User.
 * 
 * Métodos personalizados:
 * - existsByEmailIgnoreCase: Verifica se um usuário com o email fornecido (ignorando maiúsculas/minúsculas) já existe no banco de dados.
 * - findByEmailIgnoreCase: Busca um usuário pelo email fornecido (ignorando maiúsculas/minúsculas) e retorna um Optional contendo o usuário, se encontrado.
 * - existsByPhoneNumber: Verifica se um usuário com o número de telefone fornecido já existe no banco de dados.
*/