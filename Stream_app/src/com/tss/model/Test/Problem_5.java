package com.tss.model.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Problem_5 {

	public static void main(String[] args) {

		 List<String> sentences = Arrays.asList("Hello world", "hello Java", "Stream API");

	        Set<String> uniqueWords = sentences.stream()
	            .flatMap(start -> Arrays.stream(start.toLowerCase().split("\\s+")))
	            .collect(Collectors.toCollection(TreeSet::new));

	        System.out.println(uniqueWords);
	}

}
