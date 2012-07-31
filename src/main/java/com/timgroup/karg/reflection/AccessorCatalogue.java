package com.timgroup.karg.reflection;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.timgroup.karg.reference.Getter;
import com.timgroup.karg.reference.Lens;

public class AccessorCatalogue<O> {
    
    private static final Predicate<Accessor<?, ?>> isMutable = new Predicate<Accessor<?, ?>>() {
        @Override public boolean apply(Accessor<?, ?> accessor) {
            return accessor.isMutable();
        }
    };
    
    public static <O> AccessorCatalogue<O> forClass(Class<O> targetClass) {
        Map<String, Accessor<O, ?>> fieldAccessors = FieldAccessorFinder.forClass(targetClass).find();
        Map<String, Accessor<O, ?>> propertyAccessors = PropertyAccessorFinder.forClass(targetClass).find();
        
        Set<String> fieldOnlyAccessorNames = Sets.difference(fieldAccessors.keySet(), propertyAccessors.keySet());
        
        ImmutableMap<String, Accessor<O, ?>> mergedAccessors = ImmutableMap.<String, Accessor<O, ?>>builder()
            .putAll(propertyAccessors)
            .putAll(Maps.filterKeys(fieldAccessors, Predicates.in(fieldOnlyAccessorNames)))
            .build();
        
        return new AccessorCatalogue<O>(mergedAccessors);
    }

    private ImmutableMap<String, Accessor<O, ?>> getters;
    private ImmutableMap<String, Accessor<O, ?>> lenses;
    
    private AccessorCatalogue(Map<String, Accessor<O, ?>> accessors) {
        lenses = ImmutableMap.<String, Accessor<O, ?>>builder().putAll(Maps.filterValues(accessors, isMutable)).build();
        getters = ImmutableMap.<String, Accessor<O, ?>>builder().putAll(accessors).build();
    }

    public ImmutableMap<String, Accessor<O, ?>> readOnlyAttributes() {
        return getters;
    }
    
    public ImmutableMap<String, Accessor<O, ?>> allAttributes() {
        return lenses;
    }

    @SuppressWarnings("unchecked")
    public <T> Lens<O, T> getAttribute(String name) {
        return (Lens<O, T>) lenses.get(name);
    }
    
    @SuppressWarnings("unchecked")
    public <T> Getter<O, T> getReadOnlyAttribute(String name) {
        return (Getter<O, T>) getters.get(name);
    }
    
    @SuppressWarnings("unchecked")
    public <T> Accessor<O, T> getAccessor(String name) {
        return (Accessor<O, T>) getters.get(name);
    }
}
