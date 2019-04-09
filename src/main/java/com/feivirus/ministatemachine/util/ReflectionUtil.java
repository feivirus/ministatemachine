package com.feivirus.ministatemachine.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
	
	public static <T> Constructor<T> getConstructor(Class<T> type, Class<?>[] paramTypes) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor(paramTypes);
			return constructor;
		} catch (NoSuchMethodException exception) {
			
		}
		return null;
	}
	
	public static <T> T newInstance(Constructor<T> constructor) {
		return newInstance(null, constructor, null);
	}
	
	public static <T> T newInstance(Constructor<T> constructor, Object[] args) {
		return newInstance(null, constructor, args);
	}
	
	public static <T> T newInstance(Class<T> clazz, Constructor<T> constructor, Object[] args) {
		if ((clazz == null) && (constructor == null)) {
			throw new IllegalArgumentException("cannot new instance without clazz and constructor");
		}
		if (constructor == null) {
			constructor = getConstructor(clazz, (Class[])null);
		}
		
		boolean oldAccessible = constructor.isAccessible();
		try {
			if (!oldAccessible) {
				constructor.setAccessible(true);
			}
			return constructor.newInstance(args);
		} catch (Exception ex) {
			
		} finally {
			constructor.setAccessible(oldAccessible);
		}
		return null;
	}
	
	public static Object invoke(Method method, Object target, Object[] args) {
		if (method == null) {
			throw new IllegalArgumentException();
		}
		boolean oldAccessible = method.isAccessible();
		
		try {
			if (method.isAccessible() == false) {
				method.setAccessible(true);
			}
			return method.invoke(target, args);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			method.setAccessible(oldAccessible);
		}
	}
}
