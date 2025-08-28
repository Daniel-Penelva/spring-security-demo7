package com.api.spring_security_demo7.user;

import static jakarta.persistence.GenerationType.UUID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import com.api.spring_security_demo7.role.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String LastName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PHONE_NUMBER", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "IS_ENABLED")
    private boolean enabled;  // para verificar se o usuario esta ativo ou nao na aplicacao (se esta com a conta verificada)

    @Column(name = "IS_ACCOUNT_LOCKED")
    private boolean locked;  // para verificar se o usuario esta bloqueado ou nao na aplicacao (se ele errou a senha varias vezes)

    @Column(name = "CREDENTIALS_EXPIRED")
    private boolean credentialsExpired; // para verificar se as credenciais do usuario estao expiradas ou nao na aplicacao (se ele nao alterou a senha por um periodo longo)

    @Column(name = "PROFILE_PICTURE_URL")
    private String profilePictureUrl; // para armazenar a url da foto de perfil do usuario na aplicacao

    @Column(name = "IS_EMAIL_VERIFIED")
    private boolean emailVerified; // para verificar se o email do usuario esta verificado ou nao na aplicacao

    @Column(name = "IS_PHONE_VERIFIED")
    private boolean phoneVerified; // para verificar se o telefone do usuario esta verificado ou nao na aplicacao

    @CreatedDate  // OBS. Está anotação é usada para marcar o campo que deve ser preenchido automaticamente com a data de criação do registro.
    @Column(name = "CREATED_DATE", updatable = false, nullable = false)
    private LocalDateTime createdDate;  // para armazenar a data de criacao do usuario na aplicacao 

    @LastModifiedDate  // OBS. Está anotação é usada para marcar o campo que deve ser preenchido automaticamente com a data da ultima modificação do registro.
    @Column(name = "LAST_MODIFIED_DATE", insertable = false)
    private LocalDateTime lastModifiedDate; // para armazenar a data da ultima modificacao do usuario na aplicacao 


    // Muitos usuarios podem ter muitos papeis (roles) e muitos papeis (roles) podem ter muitos usuarios
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
        name = "USERS_ROLES",
        joinColumns = {
            @JoinColumn(name = "USERS_ID")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "ROLES_ID")
        }
    )
    private List<Role> roles; // para armazenar os papeis do usuario na aplicacao (ex: ADMIN, USER, etc)


    // Metodos sobrescritos da interface UserDetails do Spring Security

    /**
     * Este método retorna se a conta do usuário não está bloqueada.
     * @return true se a conta não está bloqueada, false caso contrário. 
    */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(this.roles)) {
            return List.of();
        } 
        return this.roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .toList();
    }


    /**
     * Este método retorna o nome de usuário do usuário, que neste caso é o email.
     * @return o email do usuário como nome de usuário.
    */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Este método retorna a senha do usuário.
     * @return a senha do usuário.
    */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Este método retorna se as credenciais do usuário não estão expiradas.
     * @return true se as credenciais não estão expiradas, false caso contrário.
    */
    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    /**
     * Este método retorna se o usuário está habilitado.
     * @return true se o usuário está habilitado, false caso contrário.
    */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Este método retorna se a conta do usuário não está bloqueada.
     * @return true se a conta não está bloqueada, false caso contrário.
    */
    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    /**
     * Este método retorna se a conta do usuário não está expirada.
     * @return true se a conta não está expirada, false caso contrário.
    */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    // Metodo utilitario para retornar o nome completo do usuario
    public String getFullName() {
        return this.firstName + " " + this.LastName;
    }
    
}


/**
 * Esta classe representa a entidade "User" (Usuário) no sistema de segurança. Ela contém vários campos que armazenam informações
 * relevantes sobre o usuário, como nome, email, telefone, senha, data de nascimento, status da conta, foto de perfil, entre outros. 
*/

/** UserDetails
 * Implementa a interface UserDetails do Spring Security, que é usada para fornecer informações de autenticação e autorização. 
 * Os métodos sobrescritos fornecem detalhes sobre o usuário, como suas autoridades (roles), nome de usuário (email), senha e status da conta.
*/
