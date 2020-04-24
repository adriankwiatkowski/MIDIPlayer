package com.example.midiplayer.utils;

public class RandomUtils {

    public static boolean[] generateRandomBooleanArray(int size) {
        boolean[] randomBooleanArray = new boolean[size];
        for (int i = 0; i < randomBooleanArray.length; i++) {
            if (Math.random() >= .85) {
                randomBooleanArray[i] = true;
            }
        }
        return randomBooleanArray;
    }
}
