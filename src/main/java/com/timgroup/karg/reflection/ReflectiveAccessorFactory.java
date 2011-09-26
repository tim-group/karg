package com.timgroup.karg.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.timgroup.karg.reference.Getter;
import com.timgroup.karg.reference.Lens;
import com.timgroup.karg.reference.Setter;

import static java.lang.String.format;

public class ReflectiveAccessorFactory<O> {
    
    private final ClassInspector<O> inspector;
    
    public static <O> ReflectiveAccessorFactory<O> forClass(Class<O> targetClass) {
        return new ReflectiveAccessorFactory<O>(targetClass);
    }
    
    private ReflectiveAccessorFactory(Class<O> targetClass) {
        inspector = ClassInspector.forClass(targetClass);
    }
    
    public <T> Lens<O, T> getLens(final String propertyName) {
        final Getter<O, T> getter = getGetter(propertyName);
        final Setter<O, T> setter = getSetter(propertyName);
        return new Lens<O, T>() {
            @Override public T get(O object) {
                return getter.get(object);
            }
            @Override public T set(O object, T value) {
                return setter.set(object, value);
            }
        };
    }
    
    public <T> Getter<O, T> getGetter(String propertyName) {
        Method getterMethod = inspector.findGetterMethod(propertyName);
        if (getterMethod != null) {
            return new ReflectiveAccessorFactory.MethodReflectiveGetter<O, T>(getterMethod);
        }
        Field readableField = inspector.findReadableField(propertyName);
        if (readableField != null) {
            return new ReflectiveAccessorFactory.FieldReflectiveGetter<O, T>(readableField);
        }
        throw new RuntimeException(format("No readable property or field \"%s\"", propertyName));
    }
    
    public <T> Setter<O, T> getSetter(String propertyName) {
        Method setterMethod = inspector.findSetterMethod(propertyName);
        if (setterMethod != null) {
            return new ReflectiveAccessorFactory.MethodReflectiveSetter<O, T>(setterMethod);
        }
        Field writableField = inspector.findWritableField(propertyName);
        if (writableField != null) {
            return new ReflectiveAccessorFactory.FieldReflectiveSetter<O, T>(writableField);
        }
        throw new RuntimeException(String.format("No writable property or field \"%s\""));
    }
    
    private static class MethodReflectiveGetter<O, T> implements Getter<O, T> {
        private final Method getterMethod;
        public MethodReflectiveGetter(Method getterMethod) {
            this.getterMethod = getterMethod;
        }
        
        @SuppressWarnings("unchecked")
        @Override public T get(O object) {
            try {
                return (T) getterMethod.invoke(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private static class FieldReflectiveGetter<O, T> implements Getter<O, T> {
        private final Field field;
        public FieldReflectiveGetter(Field field) {
            this.field = field;
        }
        
        @SuppressWarnings("unchecked")
        @Override public T get(O object) {
            try {
                return (T) field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private static class MethodReflectiveSetter<O, T> implements Setter<O, T> {
        private final Method setterMethod;
        public MethodReflectiveSetter(Method setterMethod) {
            this.setterMethod = setterMethod;
        }
        
        @Override public T set(O object, T value) {
            try {
                setterMethod.invoke(object, value);
                return value;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private static class FieldReflectiveSetter<O, T> implements Setter<O, T> {
        private final Field field;
        public FieldReflectiveSetter(Field field) {
            this.field = field;
        }
        
        @Override public T set(O object, T value) {
            try {
                field.set(object, value);
                return value;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}