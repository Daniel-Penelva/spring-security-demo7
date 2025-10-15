package com.api.spring_security_demo7.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

    Optional<Role> findByName(String roleUser);
}
