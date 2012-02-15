package com.timgroup.karg.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class AccessorCatalogue<O> {
    
    private static final Predicate<Accessor<?, ?>> MUTABLE = new Predicate<Accessor<?, ?>>() {
        @Override public boolean apply(Accessor<?, ?> accessor) {
            return accessor.isMutable();
        }
    };

    public static <O> AccessorCatalogue<O> forClass(Class<O> targetClass) {
        Map<String, Accessor<O, ?>> fieldAccessors = findFieldAccessors(targetClass);
        Map<String, Accessor<O, ?>> accessors = findPropertyAccessors(targetClass);
        
        Set<String> fieldOnlyAccessorNames = Sets.difference(fieldAccessors.keySet(), accessors.keySet());
        for(String fieldOnlyAccessorName : fieldOnlyAccessorNames) {
            accessors.put(fieldOnlyAccessorName, fieldAccessors.get(fieldOnlyAccessorName));
        }
        
        return new AccessorCatalogue<O>(accessors);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <O> Map<String, Accessor<O, ?>> findPropertyAccessors(Class<O> targetClass) {
        Map<String, GetterMethod<O, ?>> getterMethods = findGetterMethods(targetClass);
        Map<String, SetterMethod<O, ?>> setterMethods = findSetterMethods(targetClass);
        Map<String, Accessor<O, ?>> accessors = Maps.newHashMap();

        for (GetterMethod<O, ?> getterMethod : getterMethods.values()) {
            String propertyName = getterMethod.propertyName();
            if (setterMethods.containsKey(propertyName)) {
                SetterMethod setter = setterMethods.get(propertyName);
                accessors.put(propertyName,
                              PropertyAccessor.forProperty(propertyName,
                                                           getterMethod,
                                                           setter));
            } else {
                accessors.put(propertyName, PropertyAccessor.forProperty(propertyName, getterMethod));
            }
        }
        return accessors;
    }

    private static <O> Map<String, Accessor<O, ?>> findFieldAccessors(Class<O> targetClass) {
        Map<String, Accessor<O, ?>> fieldAccessors = Maps.newHashMap();
        for (Field field: targetClass.getFields()) {
            if (Modifier.isPublic(field.getModifiers())) {
                FieldAccessor<O, ?> accessor = FieldAccessor.forField(field);
                fieldAccessors.put(accessor.propertyName(), accessor);
            }
        }
        return fieldAccessors;
    }
    
    private static <O> Map<String, GetterMethod<O, ?>> findGetterMethods(Class<O> targetClass) {
        Map<String, GetterMethod<O, ?>> getterMethods = Maps.newHashMap();
        for (Method method: targetClass.getMethods()) {
            if (isGetter(method)) {
                GetterMethod<O, ?> getterMethod = GetterMethod.forMethod(method);
                getterMethods.put(getterMethod.propertyName(), getterMethod);
            }
        }
        return getterMethods;
    }
    
    private static <O> Map<String, SetterMethod<O, ?>> findSetterMethods(Class<O> targetClass) {
        Map<String, SetterMethod<O, ?>> setterMethods = Maps.newHashMap();
        for (Method method: targetClass.getMethods()) {
            if (isSetter(method)) {
                SetterMethod<O, ?> setterMethod = SetterMethod.forMethod(method);
                setterMethods.put(setterMethod.propertyName(), setterMethod);
            }
        }
        return setterMethods;
    }
    
    private static boolean isGetter(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (method.getParameterTypes().length > 0) {
            return false;
        }
        return !method.getReturnType().equals(Void.TYPE);
    }
    
    private static boolean isSetter(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        return method.getName().startsWith("set");
    }

    private ImmutableMap<String, Accessor<O, ?>> getters;
    private ImmutableMap<String, Accessor<O, ?>> lenses;
    
    private AccessorCatalogue(Map<String, Accessor<O, ?>> accessors) {
        lenses = ImmutableMap.<String, Accessor<O, ?>>builder().putAll(Maps.filterValues(accessors, MUTABLE)).build();
        getters = ImmutableMap.<String, Accessor<O, ?>>builder().putAll(accessors).build();
    }

    public ImmutableMap<String, Accessor<O, ?>> readOnlyAttributes() {
        return getters;
    }
    
    public ImmutableMap<String, Accessor<O, ?>> allAttributes() {
        return lenses;
    }

    @SuppressWarnings("unchecked")
    public <T> Accessor<O, T> getAttribute(String name) {
        return (Accessor<O, T>) lenses.get(name);
    }
    
    @SuppressWarnings("unchecked")
    public <T> Accessor<O, T> getReadOnlyAttribute(String name) {
        return (Accessor<O, T>) getters.get(name);
    }
}
