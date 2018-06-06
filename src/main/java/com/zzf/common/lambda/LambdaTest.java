package com.zzf.common.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class LambdaTest {

	static List<Apple> repos = Arrays.asList(new Apple(100, "red"), new Apple(80, "blue"), new Apple(120, "yellow"),
			new Apple(60, "green"), new Apple(180, "yellow"));

	public static void main(String[] args) {
		System.out.println(myFilter(apple -> "yellow".equals(apple.getColor())));
		System.out.println(myFilter(apple -> apple.getWeight() < 100));

		System.out.println(repos.stream().filter(apple -> apple.getWeight() < 100));
	}

	private static List<Apple> myFilter(ApplePredicateInterface predicate) {
		List<Apple> list = new ArrayList<Apple>();
		for (Apple apple : repos) {
			if (predicate.test(apple)) {
				list.add(apple);
			}
		}
		return list;
	}

	private static List<Apple> filter(Predicate<Apple> predicate) {
		List<Apple> list = new ArrayList<Apple>();
		for (Apple apple : repos) {
			if (predicate.test(apple)) {
				list.add(apple);
			}
		}
		return list;
	}

}
