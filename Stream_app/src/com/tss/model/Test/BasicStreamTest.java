package com.tss.model.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicStreamTest {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(2, 90, 5, 8, 9);

        numbers.stream().forEach(number -> System.out.println(number));

        numbers.stream()
               .filter(number -> number % 2 != 0)
               .forEach(number -> System.out.println(number + " odd"));

        List<Integer> oddNumbers = numbers.stream()
                                          .filter(number -> number % 2 != 0)
                                          .collect(Collectors.toList());
        System.out.println(oddNumbers);

        Set<Integer> squareNumbers = numbers.stream()
                                            .map(number -> number * number)
                                            .collect(Collectors.toSet());
        System.out.println(squareNumbers);

        List<Integer> evenNumberSquare = numbers.stream()
                                                .filter(number -> number % 2 == 0)
                                                .map(number -> number * number)
                                                .collect(Collectors.toList());
        System.out.println(evenNumberSquare + " even");

        numbers.stream().forEach(number -> System.out.println(number));

        numbers.stream().forEach(number -> System.out.println(number * number));

        int sum_1 = numbers.stream()
                           .reduce(0, (number1, number2) -> number1 + number2);
        System.out.println(sum_1 + " sums");

        System.out.println(numbers.stream().count());

        numbers.stream()
               .limit(3)
               .forEach(number -> System.out.println(number + "\t"));

        List<Integer> skip_number = numbers.stream()
                                           .skip(8)
                                           .collect(Collectors.toList());
        System.out.println(skip_number);

        System.out.println("Skip first 3 elements:");
        numbers.stream()
               .skip(3)
               .forEach(System.out::println);

        boolean anyGreaterThan8 = numbers.stream()
                                         .anyMatch(n -> n > 8);
        System.out.println("Any number greater than 8: " + anyGreaterThan8);

        boolean allLessThan20 = numbers.stream()
                                       .allMatch(n -> n < 20);
        System.out.println("All numbers less than 20: " + allLessThan20);

        System.out.println("Sorted list:");
        numbers.stream()
               .sorted()
               .forEach(System.out::println);

        System.out.println(numbers.stream().max(Integer::compareTo)); 

         System.out.println(numbers.stream().min(Integer::compareTo));
    }
}
