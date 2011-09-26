package com.timgroup.karg.reference;

import com.google.common.base.Function;

public class ConcurrentRef<T> implements Updatable<T> {
    
    public static <T> Updatable<T> to(Updatable<T> updatable) {
        return updatable;
    }
    
    public static <T> Updatable<T> to(Ref<T> ref) {
        return new ConcurrentRef<T>(ref);
    }
    
    public static interface ObjectCapture<O, T> {
        Updatable<T> of(O object);
    }
    
    public static <O, T> ObjectCapture<O, T> to(final Lens<O, T> lens) {
        return new ObjectCapture<O, T>() {
            @Override public Updatable<T> of(O object) {
                return to(Lenses.bind(lens).to(object));
            }
        };
    }
    
    private final Ref<T> innerRef;
    
    private ConcurrentRef(Ref<T> innerRef) {
        this.innerRef = innerRef;
    }
    
    @Override
    public synchronized T get() {
        return innerRef.get();
    }

    @Override
    public synchronized T set(T newValue) {
        return innerRef.set(newValue);
    }

    @Override
    public synchronized T updateTo(T newValue) {
        return set(newValue);
    }
    
    @Override
    public synchronized T updateWith(Function<T, T> updater) {
        return set(updater.apply(get()));
    }
    
    @Override
    public synchronized T swapFor(T newValue) {
        T oldValue = get();
        updateTo(newValue);
        return oldValue;
    }
    
    @Override
    public synchronized T swapWith(Function<T, T> updater) {
        T oldValue = get();
        updateWith(updater);
        return oldValue;
    }

}
