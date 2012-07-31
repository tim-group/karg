package com.timgroup.karg.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class PropertyAccessorFinder<O> {
    
    public static <O> PropertyAccessorFinder<O> forClass(Class<O> targetClass) {
        return new PropertyAccessorFinder<O>(targetClass);
    }
    
    private final Class<O> targetClass;
    
    private PropertyAccessorFinder(Class<O> targetClass) {
        this.targetClass = targetClass;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Accessor<O, ?>> find() {
        Map<String, GetterMethod<O, ?>> getterMethods = findGetterMethods(targetClass);
        Map<String, SetterMethod<O, ?>> setterMethods = findSetterMethods(targetClass);
        ImmutableMap.Builder<String, Accessor<O, ?>> accessors = ImmutableMap.builder();

        Set<String> readWritePropertyNames = Sets.intersection(getterMethods.keySet(), setterMethods.keySet());
        Set<String> readOnlyPropertyNames = Sets.difference(getterMethods.keySet(), readWritePropertyNames);
        Set<String> writeOnlyPropertyNames = Sets.difference(setterMethods.keySet(), getterMethods.keySet());
        
        Preconditions.checkState(writeOnlyPropertyNames.isEmpty(),
                                 "Write-only properties detected: %s",
                                 Joiner.on(", ").join(writeOnlyPropertyNames));
        
        for (String propertyName : readOnlyPropertyNames) {
            accessors.put(propertyName,
                          PropertyAccessor.forProperty(propertyName,
                                                       getterMethods.get(propertyName)));
        }
        
        for (String propertyName : readWritePropertyNames) {
            GetterMethod getterMethod = (GetterMethod) getterMethods.get(propertyName);
            SetterMethod setterMethod = (SetterMethod) setterMethods.get(propertyName);
            
            Preconditions.checkState(getterMethod.returnType().equals(setterMethod.valueType()), "Getter and setter for property %s do not have the same types", propertyName);
            
            accessors.put(propertyName,
                          PropertyAccessor.forProperty(propertyName,
                                                       getterMethod,
                                                       setterMethod));
        }
         
        return accessors.build();
    }
    
    private Map<String, GetterMethod<O, ?>> findGetterMethods(Class<O> targetClass) {
        ImmutableMap.Builder<String, GetterMethod<O, ?>> getterMethods = ImmutableMap.builder();
        for (Method method: Iterables.filter(Lists.newArrayList(targetClass.getMethods()), isGetterMethod)) {
            GetterMethod<O, ?> getterMethod = GetterMethod.forMethod(method);
            getterMethods.put(getterMethod.propertyName(), getterMethod);
        }
        return getterMethods.build();
    }
    
    private static <O> Map<String, SetterMethod<O, ?>> findSetterMethods(Class<O> targetClass) {
        ImmutableMap.Builder<String, SetterMethod<O, ?>> setterMethods = ImmutableMap.builder();
        for (Method method: Iterables.filter(Lists.newArrayList(targetClass.getMethods()), isSetterMethod)) {
            SetterMethod<O, ?> setterMethod = SetterMethod.forMethod(method);
            setterMethods.put(setterMethod.propertyName(), setterMethod);
        }
        return setterMethods.build();
    }
    
    private static final Predicate<Method> isGetterMethod = new Predicate<Method>() {
        public boolean apply(Method method) {
            if (!Modifier.isPublic(method.getModifiers())) {
                return false;
            }
            if (method.getParameterTypes().length > 0) {
                return false;
            }
            return !method.getReturnType().equals(Void.TYPE);
        }
    };
    
    private static final Predicate<Method> isSetterMethod = new Predicate<Method>() {
        public boolean apply(Method method) {
            if (!Modifier.isPublic(method.getModifiers())) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            return method.getName().startsWith("set");
        }
    };
}