package com.api.spring_security_demo7.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestRestAPI {

    // http://localhost:8080/api/test/public
    @RequestMapping("/public")
    public Map<String, Object> dataTestPublic() {
        return Map.of("data", "test api - validada para todos");
    }

    // http://localhost:8080/api/test/private
    @RequestMapping("/private")
    public Map<String, Object> dataTestPrivate() {
        return Map.of("data", "test api - validada para usuários autenticados");
    }

    // http://localhost:8080/api/test/admin
    @RequestMapping("/admin")
    public Map<String, Object> dataTestAdmin() {
        return Map.of("data", "test api - validada para usuários com perfil de administrador");
    }

}
