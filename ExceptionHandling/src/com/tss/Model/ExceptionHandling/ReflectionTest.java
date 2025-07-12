package com.tss.Model.ExceptionHandling;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectionTest {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		Class<?> stringClass = Class.forName("java.lang.String");
		Method methods[] = stringClass.getMethods();
		for (Method method : methods) {
			System.out.println(method.getName() + "\t" + method.getParameterCount());
			Parameter parameters[] = method.getParameters();
			for (Parameter parameter : parameters) {
				System.out.println(parameter.getName() + "\t" + parameter.getType());
			}
		}
		Constructor constructors[] = stringClass.getConstructors();
		for (Constructor constructor : constructors) {
			System.out.println(constructor.getName() + "\t" + constructor.getParameterCount());
			Parameter parameters[] = constructor.getParameters();
			for (Parameter parameter : parameters) {
				System.out.println("\tParameter Name: " + parameter.getName() + "\tParameter: " + parameter.getType());
			}
		}

		Constructor<?> constructor = stringClass.getConstructor();
		Object strObject = constructor.newInstance();

		String str = (String) strObject;

		System.out.println("String using reflection: \"" + str + "\"");
	}

}
