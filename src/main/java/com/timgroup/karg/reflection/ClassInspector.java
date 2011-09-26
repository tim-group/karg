package com.timgroup.karg.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

import static java.lang.String.format;

public final class ClassInspector<O> {
    public static <O> ClassInspector<O> forClass(Class<O> targetClass) {
        return new ClassInspector<O>(targetClass);
    }
    
    private final Method[] methods;
    private final Field[] fields;
    
    private ClassInspector(Class<O> targetClass) {
        this.methods = targetClass.getMethods();
        this.fields = targetClass.getFields();
    }
    
    public Method findGetterMethod(String propertyName) {
        return Iterators.find(Iterators.forArray(methods),
                              Predicates.and(isPublic(), getterMethodNameMatches(propertyName)),
                              null);
    }
    
    public Method findSetterMethod(String propertyName) {
        return Iterators.find(Iterators.forArray(methods),
                              Predicates.and(isPublic(), setterMethodNameMatches(propertyName)),
                              null);
    }
    
    public Field findReadableField(String propertyName) {
        return Iterators.find(Iterators.forArray(fields),
                              Predicates.and(isPublic(), fieldNameMatches(propertyName)),
                              null);
    }
    
    public Field findWritableField(String propertyName) {
        return Iterators.find(Iterators.forArray(fields),
                              Predicates.and(isModifiableField(), fieldNameMatches(propertyName)),
                              null);
    }
    
    private Predicate<Member> isPublic() {
        return new Predicate<Member>() {
            @Override public boolean apply(Member member) {
                return Modifier.isPublic(member.getModifiers());
            }
        };
    }
    
    private Predicate<Field> isModifiableField() {
        return new Predicate<Field>() {
            @Override public boolean apply(Field field) {
                return Modifier.isPublic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
            }
        };
    }
    
    private Predicate<Method> getterMethodNameMatches(String propertyName) {
        String capitalisedPropertyName = propertyName.substring(0, 1).toUpperCase().concat(propertyName.substring(1));
        final Pattern pattern = Pattern.compile(format("%s|is%s|get%s", propertyName, capitalisedPropertyName, capitalisedPropertyName));
        return new Predicate<Method>() {
            @Override public boolean apply(Method method) {
                return pattern.matcher(method.getName()).matches();
            }
        };
    }
    
    private Predicate<Method> setterMethodNameMatches(String propertyName) {
        String capitalisedPropertyName = propertyName.substring(0, 1).toUpperCase().concat(propertyName.substring(1));
        final String setterMethodName = "set" + capitalisedPropertyName;
        return new Predicate<Method>() {
            @Override public boolean apply(Method method) {
                return method.getName().equals(setterMethodName);
            }
        };
    }
    
    private Predicate<Field> fieldNameMatches(final String propertyName) {
        return new Predicate<Field>() {
            @Override public boolean apply(Field field) {
                return field.getName().equals(propertyName);
            }
        };
    }
}