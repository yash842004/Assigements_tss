package com.tss.model.Test;

import java.util.*;
import java.util.stream.Collectors;

public class FruitTest {

    public static void main(String[] args) {

        List<String> fruit = new ArrayList<>();
        fruit.add("apple");
        fruit.add("banana");
        fruit.add("apricot");
        fruit.add("blueberry");
        fruit.add("cherry");

        Map<Character, List<String>> groupedFruits = fruit.stream()
                .collect(Collectors.groupingBy(start -> start.charAt(0)));

        System.out.println(groupedFruits);
    }
}
