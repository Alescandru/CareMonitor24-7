package com.example.hackathon2024.utils;

import java.util.List;
import java.util.OptionalDouble;

public class Utils {
    public static double listAverage(List<Integer> list) {
        OptionalDouble average = list.stream().mapToDouble(a -> a).average();
        return average.isPresent() ? average.getAsDouble() : 0;
    }

    public static <T> T lastElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null; // Better be safe
        }
        return list.get(list.size() - 1);
    }
}
