package com.timgroup.karg.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.common.base.Preconditions;

public class FieldAccessor<C, V> implements Accessor<C, V> {

    public static interface FieldNameBinder {
        public <C, V> Accessor<C, V> ofClass(Class<C> owningClass);
    }
    
    public static FieldNameBinder forField(final String fieldName) {
        return new FieldNameBinder() {
            @Override public <C, V> FieldAccessor<C, V> ofClass(Class<C> owningClass) {
                Field readableField = ClassInspector.forClass(owningClass).findReadableField(fieldName);
                Preconditions.checkNotNull(readableField, "No accessible field exists for property %s", fieldName);
                return forField(readableField);
            }
        };
    }
    
    public static <C, V> FieldAccessor<C, V> forField(Field field) {
        return new FieldAccessor<C, V>(field);
    }

    private final Field field;
    
    private FieldAccessor(Field field) {
        this.field = field;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public V get(C object) {
        try {
            return (V) field.get(object);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
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

    @Override
    public String propertyName() {
        return field.getName();
    }
    
    @Override
    public boolean isMutable() {
        return !Modifier.isFinal(field.getModifiers());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Class<V> getType() {
        return (Class<V>) field.getType();
    }

}
