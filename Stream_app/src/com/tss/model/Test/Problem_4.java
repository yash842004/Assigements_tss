package com.tss.model.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Problem_4 {
	
	public static void main(String []args) {
		 List<String> list = Arrays.asList("arjun", "aryan", "yash", "yashvi", "rohan");

	        Map<Character, Long> countMap = list.stream()
	            .collect(Collectors.groupingBy(startchar -> startchar.charAt(0), Collectors.counting()));

	        System.out.println(countMap);
	}

}
