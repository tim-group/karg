package com.timgroup.karg.reference.immutable;

import com.google.common.base.Function;


public final class ImmutableCell<T> implements ImmutableRef<ImmutableCell<T>, T> {

    public static <O, T> ImmutableRef<ImmutableCell<T>, T> of(final T value) {
        return new ImmutableCell<T>(value);
    }
    
    private T value;
    
    private ImmutableCell(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public ImmutableCell<T> set(T newValue) {
        return new ImmutableCell<T>(newValue);
    }

    @Override
    public ImmutableCell<T> update(Function<T, T> updater) {
        return new ImmutableCell<T>(updater.apply(value));
    }
}
