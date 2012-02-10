package com.timgroup.karg.reflection;

import com.google.common.base.Preconditions;
import com.timgroup.karg.reference.Getter;
import com.timgroup.karg.reference.Lens;
import com.timgroup.karg.reference.Setter;

public class ReflectiveAccessorFactory<O> {
    
    private final AccessorCatalogue<O> catalogue;
    
    public static <O> ReflectiveAccessorFactory<O> forClass(Class<O> targetClass) {
        return new ReflectiveAccessorFactory<O>(targetClass);
    }
    
    private ReflectiveAccessorFactory(Class<O> targetClass) {
        catalogue = AccessorCatalogue.forClass(targetClass);
    }
    
    @SuppressWarnings("unchecked")
    public <T> Lens<O, T> getLens(final String propertyName) {
        Lens<O, T> lens = (Lens<O, T>) catalogue.allAttributes().get(propertyName);
        Preconditions.checkNotNull(lens, "No writable property or field \"%s\"", propertyName);
        return lens;
    }
    
    @SuppressWarnings("unchecked")
    public <T> Getter<O, T> getGetter(String propertyName) {
        Getter<O, T> getter = (Getter<O, T>) catalogue.readOnlyAttributes().get(propertyName);
        Preconditions.checkNotNull(getter, "No readable property or field \"%s\"", propertyName);
        return getter;
    }
    
    @SuppressWarnings("unchecked")
    public <T> Setter<O, T> getSetter(String propertyName) {
        Lens<O, T> lens = (Lens<O, T>) catalogue.allAttributes().get(propertyName);
        Preconditions.checkNotNull(lens, "No writable property or field \"%s\"", propertyName);
        return lens;
    }
}