package com.timgroup.karg.reflection;

import java.lang.reflect.Method;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.timgroup.karg.reference.Getter;
import com.timgroup.karg.reference.Setter;

public class PropertyAccessor<C, V> implements Accessor<C, V> {
    
    public static interface PropertyNameBinder {
        <C, V> Accessor<C, V> ofClass(Class<C> owningClass);
    }

    public static PropertyNameBinder forProperty(final String propertyName) {
        return new PropertyNameBinder() {
            @Override public <C, V> Accessor<C, V> ofClass(Class<C> owningClass) {
                final Getter<C, V> getter = GetterMethod.forProperty(propertyName).ofClass(owningClass);
                Preconditions.checkNotNull(getter, "No getter method exists for property %s", propertyName);
                Method setterMethod = ClassInspector.forClass(owningClass).findSetterMethod(propertyName);
                if (setterMethod == null) {
                    return forProperty(propertyName, getter);
                }
                SetterMethod<C, V> setter = SetterMethod.forMethod(setterMethod);
                return new PropertyAccessor<C, V>(propertyName, getter, setter);
            }
        };
    }
    
    public static <C, V> PropertyAccessor<C, V> forProperty(final String propertyName, Getter<C, V> getter) {
        return new PropertyAccessor<C, V>(propertyName, getter);
    }
    
    public static <C, V> PropertyAccessor<C, V> forProperty(final String propertyName,
                                                            Getter<C, V> getter,
                                                            Setter<C, V> setter) {
        return new PropertyAccessor<C, V>(propertyName, getter, setter);
    }
    
    private final String propertyName;
    private final Getter<C, V> getter;
    private final Optional<Setter<C, V>> setter;
    
    private PropertyAccessor(String propertyName, Getter<C, V> getter) {
        this.propertyName = propertyName;
        this.getter = getter;
        this.setter = Optional.absent();
    }
    
    private PropertyAccessor(String propertyName, Getter<C, V> getter, Setter<C, V> setter) {
        this.propertyName = propertyName;
        this.getter = getter;
        this.setter = Optional.of(setter);
    }
    
    @Override
    public V get(C object) {
        return getter.get(object);
    }

    @Override
    public V set(C object, V newValue) {
        Setter<C, V> notNullSetter = setter.get();
        Preconditions.checkNotNull(notNullSetter, "Attempted to set the immutable property %s", propertyName);
        return notNullSetter.set(object, newValue);
    }

    @Override
    public String propertyName() {
        return propertyName;
    }

    @Override
    public boolean isMutable() {
        return setter.isPresent();
    }
}
