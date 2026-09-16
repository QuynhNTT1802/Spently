package com.spently.utils;

import java.security.SecureRandom;

public class GenerateCodeUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final String WITHOUT_SPECIAL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();


    public static String code(String prefix) {
        return prefix + generateRandomNumber(5);
    }

    public static String codeWithLength(String prefix,int length) {
        return prefix + generateRandomNumber(length);
    }

    public static String generateRandomNumber(int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder code = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = RANDOM.nextInt(DIGITS.length());
            code.append(DIGITS.charAt(index));
        }

        return code.toString();
    }

    public static String randomChar(int count) {
        if (count <= 0) {
            return "";
        }

        StringBuilder code = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    public static String randomWithoutSpecialChar(int count) {
        if (count <= 0) {
            return "";
        }

        StringBuilder code = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = RANDOM.nextInt(WITHOUT_SPECIAL_CHARACTERS.length());
            code.append(WITHOUT_SPECIAL_CHARACTERS.charAt(index));
        }
        return code.toString();
    }

    public static String randomPassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digit = "0123456789";
        String special = "!@#$%?";
        String all = upper + lower + digit + special;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(lower.charAt(random.nextInt(lower.length())));
        sb.append(digit.charAt(random.nextInt(digit.length())));
        sb.append(special.charAt(random.nextInt(special.length())));

        for (int i = 4; i < length; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }

        char[] chars = sb.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
}
