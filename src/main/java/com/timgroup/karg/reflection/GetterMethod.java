/**
 * 
 */
package com.timgroup.karg.reflection;

import static com.timgroup.karg.naming.StringFunctions.UNCAPITALISE;
import static java.lang.String.format;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.base.Preconditions;
import com.timgroup.karg.reference.Getter;

public class GetterMethod<C, V> implements Getter<C, V>, PropertyMethod {

    private final Method method;
    private final String propertyName;
    
    public static interface PropertyNameBinder {
        <C, V> GetterMethod<C, V> ofClass(Class<C> owningClass);
    }
    
    public static PropertyNameBinder forProperty(final String propertyName) {
        return new PropertyNameBinder() {
            @Override public <C, V> GetterMethod<C, V> ofClass(Class<C> owningClass) {
                Method method = ClassInspector.forClass(owningClass).findGetterMethod(propertyName);
                Preconditions.checkNotNull(method,
                                           format("No getter method exists for the property %s", propertyName));
                return forMethod(propertyName, method);
            }
        };
    }
    
    public static <C, V> GetterMethod<C, V> forMethod(String propertyName, Method method) {
        return new GetterMethod<C, V>(propertyName, method);
    }
    
    public static <C, V> GetterMethod<C, V> forMethod(Method method) {
        return forMethod(getPropertyName(method.getName()), method);
    }
    
    private static String getPropertyName(String methodName) {
        if (methodName.startsWith("is")) {
            return UNCAPITALISE.apply(methodName.substring(2));
        }
        if (methodName.startsWith("get")) {
            return UNCAPITALISE.apply(methodName.substring(3));
        }
        return methodName;
    }
    
    private GetterMethod(String propertyName, Method method) {
        this.method = method;
        this.propertyName = propertyName;
    }
    
    @Override
    public String propertyName() {
        return propertyName;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public V get(C object) {
        try {
            return (V) method.invoke(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    
}
