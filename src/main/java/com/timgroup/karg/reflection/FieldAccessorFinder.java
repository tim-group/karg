package com.timgroup.karg.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public final class FieldAccessorFinder<O> {
    
    public static <O> FieldAccessorFinder<O> forClass(Class<O> targetClass) {
        return new FieldAccessorFinder<O>(targetClass);
    }
    
    private final Class<O> targetClass;
    
    private FieldAccessorFinder(Class<O> targetClass) {
        this.targetClass = targetClass;
    }
    
    public Map<String, Accessor<O, ?>> find() {
        ImmutableMap.Builder<String, Accessor<O, ?>> builder = ImmutableMap.builder();
        for (Field field: targetClass.getFields()) {
            if (Modifier.isPublic(field.getModifiers())) {
                FieldAccessor<O, ?> accessor = FieldAccessor.forField(field);
                builder.put(accessor.propertyName(), accessor);
            }
        }
        return builder.build();
    }
}