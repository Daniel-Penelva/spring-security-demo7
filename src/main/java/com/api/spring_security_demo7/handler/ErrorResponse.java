package com.api.spring_security_demo7.handler;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorResponse {

    String message;
    private String code;
    private List<ValidationError> validationErrors;


    /**
     * Classe interna para representar erros de validação específicos. 
     * Cada instância desta classe contém detalhes sobre um erro de validação, incluindo o campo afetado, ou seja, o nome do campo que causou o erro,
     * o código do erro, que pode ser usado para identificar o tipo de erro, e a mensagem de erro, que fornece uma descrição legível do problema.
     * Isso é útil para fornecer feedback detalhado ao usuário ou ao cliente da API sobre quais campos específicos falharam na validação e por quê. 
     * Essa estrutura facilita a comunicação clara de problemas de validação em formulários ou dados enviados para a aplicação.
     * Exemplo de uso: Se um usuário tentar se registrar com um email inválido, uma instância de ValidationError pode ser criada com o campo "email",
     * o código de erro correspondente e uma mensagem explicando que o email fornecido não é válido.
     * 
    */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ValidationError {
        private String field;
        private String code;
        private String message;
    }

}

/**
 * Atenção!
 * Esta classe é usada para estruturar respostas de erro em uma aplicação, especialmente em APIs RESTful.
 * Ela encapsula informações sobre o erro ocorrido, incluindo uma mensagem geral, um código de erro específico e uma lista de erros de validação detalhados.
 * A classe utiliza anotações do Lombok para reduzir o boilerplate, como getters, construtores e o padrão builder.
 * Isso facilita a criação e o gerenciamento de respostas de erro, tornando-as mais consistentes e fáceis de entender para os consumidores da API.
 * A inclusão de uma lista de ValidationError permite fornecer feedback detalhado sobre erros específicos em campos individuais, o que é particularmente útil para validação de dados de entrada.
*/
