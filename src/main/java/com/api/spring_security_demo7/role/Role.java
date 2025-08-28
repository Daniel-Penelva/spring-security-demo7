package com.api.spring_security_demo7.role;

import java.util.List;

import com.api.spring_security_demo7.common.BaseEntity;
import com.api.spring_security_demo7.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Role extends BaseEntity{

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    
}

/**
 * Esta classe representa a entidade "Role" (Papel) no sistema de segurança. Ela estende a classe BaseEntity, herdando assim os campos comuns 
 * como id, createdDate, lastModifiedDate, createdBy e lastModifiedBy. 
 * 
 * A entidade Role possui um campo adicional "name" que representa o nome do papel (ex: ADMIN, USER, etc) e uma lista de usuários associados a 
 * esse papel, estabelecendo uma relação muitos-para-muitos com a entidade User. 
 * 
 * A anotação @Entity indica que esta classe é uma entidade JPA, e a anotação @Table especifica o nome da tabela no banco de dados onde os dados 
 * dessa entidade serão armazenados.
*/
