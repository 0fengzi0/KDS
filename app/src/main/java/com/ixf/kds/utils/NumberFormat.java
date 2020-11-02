package com.ixf.kds.utils;

public class NumberFormat {
    public static String number2String4(int number) {
        StringBuilder formatNumber = new StringBuilder();
        int numberLength = ("" + number).length();
        formatNumber = new StringBuilder(number + "");
        for (int i = 0; i < 4 - numberLength; i++) {
            formatNumber.insert(0, "0");
        }
        MLog.debug("数字转换", "数字为" + numberLength + "位," + number + "===" + formatNumber);
        return formatNumber.toString();
    }
}
