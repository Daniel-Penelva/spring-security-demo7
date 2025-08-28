package com.api.spring_security_demo7.common;

import static jakarta.persistence.GenerationType.UUID;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE", insertable = false)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", insertable = false)
    private String lastModifiedBy;

}

/**
 * Está classe é uma entidade base que pode ser estendida por outras entidades JPA. Ela contém campos comuns que são frequentemente usados em 
 * várias entidades, como id, createdDate, lastModified Date, createdBy e lastModifiedBy. 
 * 
 * A anotação @MappedSuperclass indica que esta classe não é uma entidade por si só, mas suas propriedades serão herdadas pelas entidades que a 
 * estendem. 
 * 
 * As anotações @CreatedDate, @LastModifiedDate, @CreatedBy e @LastModifiedBy são usadas para auditoria automática dos campos correspondentes. 
 * 
 * A anotação @EntityListeners(AuditingEntityListener.class) habilita o suporte à auditoria para esta entidade base.
*/
