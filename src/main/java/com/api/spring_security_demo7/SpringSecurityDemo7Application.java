package com.api.spring_security_demo7;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.api.spring_security_demo7.role.Role;
import com.api.spring_security_demo7.role.RoleRepository;
import com.api.spring_security_demo7.security.KeyGeneratorUtil;

@SpringBootApplication
public class SpringSecurityDemo7Application {

	public static void main(String[] args) throws Exception {

		// Gera as chaves apenas se não existirem
		KeyGeneratorUtil.generateKeysIfNotExists();

		SpringApplication.run(SpringSecurityDemo7Application.class, args);
	}

	@Bean
	public CommandLineRunner run(final RoleRepository roleRepository) {
		return args -> {

			final Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
			final Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");

			if (userRole.isEmpty()) {
				final Role role = new Role();
				role.setName("ROLE_USER");
				role.setCreatedBy("APP");
				roleRepository.save(role);
				System.out.println("Role ROLE_USER created successfully.");
			} else {
				System.out.println("Role ROLE_USER already exists.");
			}

			if (adminRole.isEmpty()) {
				final Role role = new Role();
				role.setName("ROLE_ADMIN");
				role.setCreatedBy("APP");
				roleRepository.save(role);
				System.out.println("Role ROLE_ADMIN created successfully.");
			} else {
				System.out.println("Role ROLE_ADMIN already exists.");
			}
		};
	}

}
