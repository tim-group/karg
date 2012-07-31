package com.timgroup.karg.reflection;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.timgroup.karg.reference.Setter;

public class PropertyAccessor<C, V> implements Accessor<C, V> {
    
    public static interface PropertyNameBinder {
        <C, V> Accessor<C, V> ofClass(Class<C> owningClass);
    }

    public static PropertyNameBinder forProperty(final String propertyName) {
        return new PropertyNameBinder() {
            @SuppressWarnings("unchecked")
            @Override public <C, V> Accessor<C, V> ofClass(Class<C> owningClass) {
                Accessor<C, V> accessor = (Accessor<C, V>) PropertyAccessorFinder.forClass(owningClass).find().get(propertyName);
                Preconditions.checkNotNull(accessor, "No property [%s] exists on class [%s]", propertyName, owningClass);
                return accessor;
            }
        };
    }
    
    public static <C, V> PropertyAccessor<C, V> forProperty(final String propertyName, GetterMethod<C, V> getter) {
        return new PropertyAccessor<C, V>(propertyName, getter);
    }
    
    public static <C, V> PropertyAccessor<C, V> forProperty(String propertyName,
                                                            GetterMethod<C, V> getter,
                                                            SetterMethod<C, V> setter) {
        return new PropertyAccessor<C, V>(propertyName, getter, setter);
    }
    
    private final String propertyName;
    private final GetterMethod<C, V> getter;
    private final Optional<SetterMethod<C, V>> setter;
    
    private PropertyAccessor(String propertyName, GetterMethod<C, V> getter) {
        this.propertyName = propertyName;
        this.getter = getter;
        this.setter = Optional.absent();
    }
    
    private PropertyAccessor(String propertyName, GetterMethod<C, V> getter, SetterMethod<C, V> setter) {
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

    @Override
    public Class<V> getType() {
        return getter.returnType();
    }
}
