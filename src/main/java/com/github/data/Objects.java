package com.github.data;

import java.util.Collection;
import java.util.Map;

public class Objects {

	public static boolean isNull(Object o) {
		return o == null;
	}

	public static boolean isNotNull(Object o) {
		return o != null;
	}

	public static <T> T defaultIfEmpty(T obj, T defaultObj) {
		if (isNull(obj)) return defaultObj;
		return obj;
	}

	public static boolean isSameObject(Object o, Object a) {
		return o == a;
	}

	public static boolean isNotSameObject(Object o, Object a) {
		return o != a;
	}

	public static <T> T getNotNull(T... ts) {
		for (T t : ts) {
			if (isNotNull(t)) return t;
		}
		return null;
	}

	public static boolean not(boolean b) {
		return !b;
	}

	public static boolean isNullOrEmpty(Collection<?> coll) {
		return isNull(coll) || coll.isEmpty();
	}

	public static boolean isNotNullOrEmpty(Collection<?> coll) {
		return not(isNullOrEmpty(coll));
	}

	public static boolean isNullOrEmpty(Map<?, ?> map) {
		return isNull(map) || map.isEmpty();
	}

	public static boolean isNotNullOrEmpty(Map<?, ?> map) {
		return not(isNullOrEmpty(map));
	}

	public static <T> boolean isNullOrEmpty(T[] array) {
		return isNull(array) || array.length == 0;
	}

	public static <T> boolean isNotNullOrEmpty(T[] array) {
		return not(isNullOrEmpty(array));
	}

}
