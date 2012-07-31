package com.timgroup.karg.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.base.Preconditions;
import com.timgroup.karg.naming.TargetName;
import com.timgroup.karg.naming.TargetNameFormatter;
import com.timgroup.karg.reference.Setter;

public class SetterMethod<C, V> implements Setter<C, V>, PropertyMethod {
    
    private final String propertyName;
    private final Method method;

    public static interface PropertyNameBinder {
        <C, V> SetterMethod<C, V> ofClass(Class<C> owningClass);
    }
    
    public static PropertyNameBinder forProperty(final String propertyName) {
        return new PropertyNameBinder() {
            @Override public <C, V> SetterMethod<C, V> ofClass(Class<C> owningClass) {
                Method setterMethod = ClassInspector.forClass(owningClass).findSetterMethod(propertyName);
                Preconditions.checkNotNull(setterMethod,
                                           "No setter method found for property %s",
                                           propertyName);
                return forMethod(propertyName, setterMethod);
            }
        };
    }
    
    public static <C, V> SetterMethod<C, V> forMethod(String propertyName, Method method) {
        return new SetterMethod<C, V>(propertyName, method);
    }
    
    public static <C, V> SetterMethod<C, V> forMethod(Method method) {
        return new SetterMethod<C, V>(getPropertyName(method), method);
    }

    private static String getPropertyName(Method method) {
        return TargetName.fromMethodName(method.getName())
                         .withoutPrefix()
                         .formatWith(TargetNameFormatter.LOWER_CAMEL_CASE);
    }
    
    private SetterMethod(String propertyName, Method method) {
        this.propertyName = propertyName;
        this.method = method;
    }
    
    @Override
    public String propertyName() {
        return propertyName;
    }

    @SuppressWarnings("unchecked")
    public Class<V> valueType() {
        return (Class<V>) method.getParameterTypes()[0];
    }
    
    @Override
    public V set(C object, V newValue) {
        try {
            method.invoke(object, newValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return newValue;
    }

}
