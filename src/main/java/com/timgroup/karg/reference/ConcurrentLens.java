package com.timgroup.karg.reference;

import com.google.common.base.Function;

public class ConcurrentLens<O, T> implements Updater<O, T> {

    public static <O, T> Updater<O, T> to(Lens<O, T> lens) {
        return new ConcurrentLens<O, T>(lens);
    }
    
    private final Lens<O, T> innerLens;
    private ConcurrentLens(Lens<O, T> innerLens) {
        this.innerLens = innerLens;
    }
    
    @Override
    public synchronized T get(O object) {
        return innerLens.get(object);
    }

    @Override
    public synchronized T set(O object, T newValue) {
        return innerLens.set(object, newValue);
    }

    @Override
    public synchronized T updateTo(O object, T newValue) {
        return set(object, newValue);
    }

    @Override
    public synchronized T updateWith(O object, Function<T, T> updater) {
        return set(object, updater.apply(get(object)));
    }

    @Override
    public synchronized T swapFor(O object, T newValue) {
        T oldValue = get(object);
        updateTo(object, newValue);
        return oldValue;
    }

    @Override
    public synchronized T swapWith(O object, Function<T, T> updater) {
        T oldValue = get(object);
        updateWith(object, updater);
        return oldValue;
    }

}
