package com.api.spring_security_demo7.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] args;

    /**
     * Construtor da exceção de negócio.
     * @param errorCode O código de erro associado a esta exceção.
     * @param args Argumentos adicionais para formatar a mensagem de erro.
    */
    public BusinessException(ErrorCode errorCode, Object... args) {
        super(getFormatterMessage(errorCode, args));
        this.errorCode = errorCode;
        this.args = args;
    }


    /**
     * Monta a mensagem formatada com os argumentos fornecidos. 
     * Se nenhum argumento for fornecido, retorna a mensagem padrão.
     * @param errorCode O código de erro contendo a mensagem padrão.
     * @param args Argumentos para formatar a mensagem.
     * @return A mensagem formatada ou a mensagem padrão se nenhum argumento for fornecido.
     */
    private static String getFormatterMessage(final ErrorCode errorCode, final Object... args) {
        if (args != null && args.length > 0) {
            return String.format(errorCode.getDefaultMessage(), args);
        }
        return errorCode.getDefaultMessage();
    }

}

/**OBS.
 * Object... args -> significa que o método pode receber um número variável de argumentos do tipo Object.
 * Esses argumentos são então armazenados em um array chamado args dentro do método.
 * Isso é útil quando você não sabe de antemão quantos argumentos serão passados para o método.
 * No contexto desta exceção, args pode ser usado para fornecer informações adicionais que podem ser inseridas na mensagem de erro formatada.
 * Por exemplo, se a mensagem de erro padrão for "User %s not found", você pode passar o nome do usuário como um argumento para substituir o %s na mensagem.
 * Assim, ao lançar a exceção, isto é poderia fazer algo como:
 * throw new BusinessException(ErrorCode.USER_NOT_FOUND, username);
 * E a mensagem resultante seria "User [username] not found".
 * Isso torna as mensagens de erro mais dinâmicas e informativas.
*/

/**
 * Atenção!
 * Esta classe é uma exceção personalizada que representa erros de negócio na aplicação.
 * Ela estende RuntimeException, permitindo que seja lançada sem a necessidade de declaração explícita.
 * A exceção encapsula um ErrorCode, que define o tipo específico de erro ocorrido, e pode aceitar argumentos adicionais para formatar a mensagem de erro.
 * Isso facilita a gestão de erros de forma consistente e estruturada, proporcionando mensagens claras e específicas para diferentes situações de erro na aplicação.
 * Essas exceções podem ser capturadas e tratadas em níveis superiores da aplicação, como em controladores REST, para fornecer respostas apropriadas aos clientes.
 * Além disso, o uso de ErrorCode permite uma padronização dos erros, facilitando a manutenção e a compreensão do código. 
*/
