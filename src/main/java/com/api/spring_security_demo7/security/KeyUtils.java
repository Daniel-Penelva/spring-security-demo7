package com.api.spring_security_demo7.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    /**
     * Construtor privado para impedir a instanciação da classe, já que ela possui apenas métodos utilitários estáticos.
    */
    private KeyUtils() {
    }


    /**
     * Carrega e retorna a chave privada RSA a partir de um arquivo PEM localizado na pasta resources do projeto.
     * Este método remove os delimitadores: {@code -----BEGIN PRIVATE KEY-----} e {@code -----END PRIVATE KEY-----}
     * do conteúdo do arquivo, além de espaços em branco, e então decodifica os bytes em um objeto {@link PrivateKey}.
     * 
     * @param pemPath   Caminho relativo do arquivo PEM dentro de resources.
     *                  Exemplo: {@code keys/local-only/private_key.pem}.
     * @return Objeto   {@link PrivateKey} pronto para ser utilizado em operações criptográficas, como assinatura de JWTs.
     * @throws Exception Caso o arquivo não seja encontrado, o conteúdo não esteja em Base64 válido ou ocorra erro na geração da chave.
    */
    public static PrivateKey loadPrivateKey(final String pemPath) throws Exception {
        final String key = readKeyFromResource(pemPath)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        final byte[] decoded = Base64.getDecoder().decode(key);
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }


    /**
     * Carrega e retorna a chave pública RSA a partir de um arquivo PEM localizado na pasta resources do projeto.
     * Este método remove os delimitadores: {@code -----BEGIN PUBLIC KEY-----} e {@code -----END PUBLIC KEY-----}
     * do conteúdo do arquivo, além de espaços em branco, e então decodifica os bytes em um objeto {@link PublicKey}.
     * 
     * @param pemPath Caminho relativo do arquivo PEM dentro de resources.
     *                Exemplo: {@code keys/local-only/public_key.pem}.
     * @return Objeto {@link PublicKey} pronto para ser utilizado em operações criptográficas, como validação de JWTs.
     * @throws Exception Caso o arquivo não seja encontrado, o conteúdo não esteja em Base64 válido ou ocorra erro na geração da chave.
     * 
    */
    public static PublicKey loadPublicKey(final String pemPath) throws Exception {
        final String key = readKeyFromResource(pemPath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        final byte[] decoded = Base64.getDecoder().decode(key);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }


    /**
     * Lê o conteúdo de um arquivo localizado na pasta resources e retorna como uma {@link String}.
     * Esse método utiliza o classloader da aplicação para buscar o recurso e lança uma exceção caso o arquivo não seja encontrado.
     * 
     * @param path Caminho relativo do arquivo dentro de resources.
     * @return Conteúdo do arquivo como {@link String}.
     * @throws Exception Caso o arquivo não seja encontrado ou ocorra erro na leitura.
    */
    private static final String readKeyFromResource(final String path) throws Exception {
        try (final InputStream is = KeyUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Key not found: " + path);
            }
            return new String(is.readAllBytes());
        }
    }

}

/**
 * Classe utilitária para carregamento de chaves RSA (pública e privada) a partir de arquivos no formato PEM.
 * Essa classe fornece métodos estáticos que permitem ler chaves localizadas na pasta resources do projeto, removendo os delimitadores do padrão 
 * PEM e convertendo-as para objetos {@link PrivateKey} e {@link PublicKey} que podem ser utilizados em operações criptográficas como assinatura 
 * e validação de JWTs.
 * 
 * OBS. Esta classe não deve ser instanciada, por isso possui um construtor privado.
*/
