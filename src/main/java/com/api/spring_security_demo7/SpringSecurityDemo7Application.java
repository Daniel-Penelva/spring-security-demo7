package com.api.spring_security_demo7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.api.spring_security_demo7.security.KeyGeneratorUtil;

@SpringBootApplication
public class SpringSecurityDemo7Application {

	public static void main(String[] args) throws Exception {

		// Gera as chaves apenas se não existirem
		KeyGeneratorUtil.generateKeysIfNotExists();

		SpringApplication.run(SpringSecurityDemo7Application.class, args);
	}

}
