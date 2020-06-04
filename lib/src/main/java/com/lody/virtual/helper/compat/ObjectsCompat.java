package com.lody.virtual.helper.compat;
public class ObjectsCompat {
	public static boolean equals(Object a, Object b) {
		return (a == null) ? (b == null) : a.equals(b);
	}
}
