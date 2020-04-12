package com.example.onepos.util;

public class MoneyUtil {

    public static double doublePlusDouble(double a, double b) {
        return a+b;
    }
    public static double doubleMinusDouble(double a, double b) {
        return ((int)(a * 100) - (int)(b * 100)) / 100;
    }
    public static double doubleTimesDouble(double a, double b) {
        return a*b;
    }
    public static double intTimesDouble(int a, double b) {
        return (a * (int)(b * 100)) / 100.0;
    }
}
