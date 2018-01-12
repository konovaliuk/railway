package com.liashenko.app.controller.utils;

public abstract class Asserts {

    public static boolean assertIsNull(Object obj) {
        return obj == null;
    }

    public static boolean assertStringIsEmpty(String str) {
        return str.isEmpty();
    }

    public static boolean assertStringIsNullOrEmpty(String str) {
        return (assertIsNull(str) || assertStringIsEmpty(str));
    }

    public static boolean assertStringSizeIsMore(String str, int size) {
        return assertIsNull(str) || str.length() > size;
    }

    public static boolean assertStringSizeIsLess(String str, int size) {
        return assertIsNull(str) || str.length() < size;
    }

    public static boolean assertArrayIsNullOrEmpty(Object[] arr) {
        return assertIsNull(arr) || (arr.length == 0);
    }

    public static boolean assertArrayIsNullOrEmpty(char[] arr) {
        return assertIsNull(arr) || (arr.length == 0);
    }
}
