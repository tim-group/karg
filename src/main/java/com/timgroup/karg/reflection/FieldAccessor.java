package com.timgroup.karg.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.timgroup.karg.reference.Lens;

public class FieldAccessor<C, V> implements Lens<C, V> {

	private final Field field;

	public static interface FieldNameBinder {
		public <C, V> FieldAccessor<C, V> ofClass(Class<C> owningClass);
	}
	
	public static FieldNameBinder forField(final String fieldName) {
		return new FieldNameBinder() {
			@Override public <C, V> FieldAccessor<C, V> ofClass(Class<C> owningClass) {
					return new FieldAccessor<C, V>(findAccessibleField(fieldName, owningClass));
			}
		};
	}
	
	private static Field findAccessibleField(String fieldName, Class<?> owningClass) {
		try {
			Field field = owningClass.getField(fieldName);
			return field;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	private FieldAccessor(Field field) {
		this.field = field;
	}
	
	@SuppressWarnings("unchecked")
	public V get(C object) {
		try {
			return (V) field.get(object);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public V set(C object, V newValue) {
		try {
			field.set(object, newValue);
			return newValue;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean mutable() {
		return !Modifier.isFinal(field.getModifiers());
	}

}
