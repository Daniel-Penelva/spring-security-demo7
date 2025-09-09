package com.api.spring_security_demo7.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class KeyGeneratorUtil {

    // Caminho onde as chaves serão armazenadas (salvas)
    private static final String KEYS_FOLDER = "src/main/resources/keys/local-only/";
    private static final String PRIVATE_KEY_FILE = KEYS_FOLDER + "private_key.pem";
    private static final String PUBLIC_KEY_FILE = KEYS_FOLDER + "public_key.pem";


    /**
     * Cria as chaves somente se elas ainda não existirem. Evita sobreescrever uma chave já em uso, garantindo persistência.
     * @throws IOException Lançada caso ocorra algum erro durante a escrita do arquivo.
    */
    public static void generateKeysIfNotExists() throws Exception{

        // Garante que a pasta existe
        createDirectoryIfNotExists();

        // Só gera as chaves se elas ainda não existirem, ou seja, verifica se os arquivos já existem
        if (Files.exists(Paths.get(PRIVATE_KEY_FILE)) && Files.exists(Paths.get(PUBLIC_KEY_FILE))) {  // Se ambos os arquivos (private_key.pem e public_key.pem) já existirem, ele não gera novos.
            System.out.println("As chaves já existem em: " + KEYS_FOLDER);                            // Isso evita sobrescrever as chaves atuais, o que poderia invalidar JWTs existentes.
            return;
        }

        // Gera as chaves RSA 2048 bits - vai criar um par de chaves (public e private) usando o algoritmo RSA.
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");  // Algoritmo RSA assimétrico
        keyGen.initialize(2048);  // Tamanho da chave 2048 bits
        KeyPair keyPair = keyGen.generateKeyPair();  // Gera o par de chaves (pública e privada) 

        // Salvar a chaves em formato PEM
        // Usa o método saveKeyAsPem para salvar cada chave no formato .pem compatível com OpenSSL.
        saveKeyAsPem(keyPair.getPrivate().getEncoded(), PRIVATE_KEY_FILE, "PRIVATE KEY");
        saveKeyAsPem(keyPair.getPublic().getEncoded(), PUBLIC_KEY_FILE, "PUBLIC KEY");

        System.out.println("Chaves geradas com sucesso!");

    }


    /**
     * Este método auxiliar transforma os bytes da chave em um formato PEM e salva em disco.
     * O formato PEM é amplamente utilizado por ferramentas como OpenSSL, permitindo que
     * a chave seja facilmente compartilhada ou utilizada em outras aplicações.
     * 
     * @param keyBytes      Os bytes da chave que serão convertidos para Base64 e salvos.
     * @param fileName      Caminho completo do arquivo onde a chave será gravada.
     * @param description   Descrição do tipo da chave, como "PRIVATE KEY" ou "PUBLIC KEY",
     * @throws IOException  Lançada caso ocorra algum erro durante a escrita do arquivo.
    */
    private static void saveKeyAsPem(byte[] keyBytes, String fileName, String description) throws IOException {
        String encodedKey = Base64.getMimeEncoder(64, "\n".getBytes()) 
                                  .encodeToString(keyBytes); // Codifica em Base64 com quebras de linha a cada 64 caracteres
        
        String pem = "-----BEGIN " + description + "-----\n"
                   + encodedKey
                   + "\n-----END " + description + "-----";
        
        Files.write(Paths.get(fileName), pem.getBytes());
    }

    /**
     * Cria o diretório se ele não existir
     * @throws IOException Lançada caso ocorra algum erro durante a escrita do arquivo.
    */
    private static void createDirectoryIfNotExists() throws IOException {
        Path path = Paths.get(KEYS_FOLDER);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Diretório criado: " + path.toAbsolutePath());
        }
    }
    
}

/**
 * Atenção!
 * Essa classe é usada para gerar, salvar e gerenciar chaves RSA que serão utilizadas na assinatura e validação de JWTs ou 
 * outros mecanismos de criptografia.
*/
