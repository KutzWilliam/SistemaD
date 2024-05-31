package Vagas;

import java.util.regex.Pattern;

public class Validador {
    // Regex para validar e-mail
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    // Regex para validar senha
    private static final String SENHA_PATTERN = "^[0-9]{3,8}$";
    // Regex para validar nome
    private static final String NOME_PATTERN = "^[a-zA-Z0-9\\s]{6,30}$";

    public static boolean validarEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    public static boolean validarSenha(String senha) {
        return Pattern.matches(SENHA_PATTERN, senha);
    }

    public static boolean validarNome(String nome) {
        return Pattern.matches(NOME_PATTERN, nome);
    }
}

