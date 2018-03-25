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
        StringBuilder sb = new StringBuilder();
        char[] chars = message.toCharArray();
        int len = chars.length;
        for (int i = 0; i < len; ++i) {
            sb.append(Integer.toBinaryString(chars[i]));
            //sb.append((int)chars[i]);
            //sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 二进制串转回ASCII
     * @param message
     * @return
     */
    public static String binaryToString(String message) {
        StringBuilder sb = new StringBuilder();
        int len = message.length();
        if (len % 7 != 0) {
            System.out.println("提取出来的不是标准长度");
        }
        for (int i = 0; i < len; i += 7) {
            int chin = Integer.parseInt(message.substring(i, i + 7), 2);
            sb.append((char)chin);
        }
        return sb.toString();
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

    /**
     * 按k位切割字符串
     * @param message
     * @param k
     * @return
     */
    public static String cutBinaryString(String message, int k) {
        if (k <= 0) {
            return "illegal k";
        }
        char[] chars = message.toCharArray();
        int len = chars.length;
        int quotient = len / k;
        int remainder = len % k;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < quotient; ++i) {
            for (int j = 0; j < k; j++) {
                sb.append(chars[i * k + j]);
            }
            sb.append(" ");
        }
        if (remainder != 0) {
            for (int i = remainder; i > 0 ; --i) {
                sb.append(chars[len - i]);
            }
        }
        return sb.toString();
    }

}
