package com.lifei.utils;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/2/20
 */

public class EnCode {
    private static final char[] DIGITS_HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * @param: [str] 字符串
     * @return: java.lang.String 十六进制串
     * @author: zhuoli
     * @date: 2018/6/4 12:51
     */
    public static String toHex(String str) {
        byte[] data = str.getBytes();
        int outLength = data.length;
        char[] out = new char[outLength << 1];
        for (int i = 0, j = 0; i < outLength; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & data[i]];
        }
        return new String(out);
    }

    /**
     * @param: [hex] 十六进制字符串
     * @return: java.lang.String String串
     * @author: zhuoli
     * @date: 2018/6/4 12:48
     */
    public static String fromHex(String hex) {
        /*兼容带有\x的十六进制串*/
        hex = hex.replace("\\x","");
        char[] data = hex.toCharArray();
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("字符个数应该为偶数");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return new String(out);
    }

    public static void main(String[] args) {
        String s = "卓立测试";
        String hex = toHex(s);
        System.out.println("原字符串:" + s);
        System.out.println("十六进制字符串:" + hex);
        String decode = fromHex("\\xE5\\x8D\\x93\\xE7\\xAB\\x8B\\xE6\\xB5\\x8B\\xE8\\xAF\\x95");
        System.out.println("解码:" + decode);
    }
}
