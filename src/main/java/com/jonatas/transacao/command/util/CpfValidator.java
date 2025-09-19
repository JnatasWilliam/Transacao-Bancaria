package com.jonatas.transacao.command.util;

public class CpfValidator {

    public static boolean isValid(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int digito1 = calcularDigito(cpf, 10);
            int digito2 = calcularDigito(cpf, 11);
            return cpf.charAt(9) - '0' == digito1 && cpf.charAt(10) - '0' == digito2;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcularDigito(String cpf, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < pesoInicial - 1; i++) {
            soma += (cpf.charAt(i) - '0') * (pesoInicial - i);
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}