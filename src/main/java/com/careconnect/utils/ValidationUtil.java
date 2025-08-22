package com.careconnect.utils;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern REPETIDOS_11 = Pattern.compile("(\\d)\\1{10}");
    private static final Pattern REPETIDOS_14 = Pattern.compile("(\\d)\\1{13}");

    private ValidationUtil(){}

    public static String onlyDigits(String raw){
        return raw == null ? null : raw.replaceAll("\\D", "");
    }

    public static boolean isValidCPF(String raw){
        if (raw == null) return false;
        String cpf = onlyDigits(raw);
        if (cpf.length() != 11 || REPETIDOS_11.matcher(cpf).matches()) return false;
        int d1 = digCpf(cpf, 9), d2 = digCpf(cpf, 10);
        return (cpf.charAt(9)-'0')==d1 && (cpf.charAt(10)-'0')==d2;
    }
    private static int digCpf(String cpf, int len){
        int soma=0, peso=len+1;
        for(int i=0;i<len;i++) soma += (cpf.charAt(i)-'0')*(peso-i);
        int dig = 11 - (soma%11);
        return dig>=10?0:dig;
    }

    public static boolean isValidCNPJ(String raw){
        if (raw == null) return false;
        String cnpj = onlyDigits(raw);
        if (cnpj.length() != 14 || REPETIDOS_14.matcher(cnpj).matches()) return false;
        int d1 = digCnpj(cnpj, 12);
        int d2 = digCnpj(cnpj, 13);
        return (cnpj.charAt(12)-'0')==d1 && (cnpj.charAt(13)-'0')==d2;
    }
    private static int digCnpj(String cnpj, int len){
        int[] pesos = (len==12)
            ? new int[]{5,4,3,2,9,8,7,6,5,4,3,2}
            : new int[]{6,5,4,3,2,9,8,7,6,5,4,3,2};
        int soma=0;
        for(int i=0;i<len;i++) soma += (cnpj.charAt(i)-'0')*pesos[pesos.length-len+i];
        int dig = soma%11;
        return dig<2?0:11-dig;
    }
}
