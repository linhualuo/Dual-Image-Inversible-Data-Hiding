package com.hualuo.util;

/**
 * 字符串工具
 *
 * @author Joseph
 * @create 2018/3/25 16:20
 */
public class StringUtils {

    /**
     * 获取二进制串
     * @param message
     * @return
     */
    public static String getBinaryString(String message) {
        if (message == "" || message == null) {
            return "";
        }
        return cnToUnicode(message);
    }

    /**
     * 二进制串转回ASCII
     * @param message
     * @return
     */
    public static String binaryToString(String message) {
        int len = message.length();
        if (len % 16 != 0) {
            System.out.println("提取出来的不是标准长度");
        }
        return unicodeToCn(message);
    }

    /**
     * 通过给定数字和k的值，返回一个k位的二进制串
     * 前提：num < 2 ^ k
     * @param num
     * @param k
     * @return
     */
    public static String getBinaryByK(int num, int k) {
        StringBuilder sb = new StringBuilder(k);
        for (int i = 0; i < k; ++i) {
            sb.append(num & 0x01);
            num >>= 1;
        }
        return sb.reverse().toString();
    }

    private static String cnToUnicode(String message) {
        char[] chars = message.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; ++i) {
            stringBuilder.append(fixChar(Integer.toString(chars[i], 2), 16));
        }
        return stringBuilder.toString();
    }

    private static String unicodeToCn(String message) {
        String[] strings = new String[message.length() / 16];
        int index = 0;
        for (int i = 0; i < strings.length; i++) {
            strings[i] = message.substring(index, index + 16);
            index += 16;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; ++i) {
            stringBuilder.append((char)Integer.valueOf(strings[i], 2).intValue());
        }
        return stringBuilder.toString();
    }

    private static String fixChar(String str, int radix) {
        int len = str.length();
        StringBuilder stringBuilder = new StringBuilder();
        while (radix - len++ > 0) {
            stringBuilder.append("0");
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

}
