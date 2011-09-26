package com.timgroup.karg.reference;

import com.google.common.base.Function;

public final class Updaters {

    public static interface ObjectCapture<O, T> {
        Updatable<T> to(O object);
    }
    
    public static <O, T> ObjectCapture<O, T> bind(final Updater<O, T> updater) {
        return new ObjectCapture<O, T>() {
            @Override
            public Updatable<T> to(final O object) {
                return new Updatable<T>() {
                    @Override public T get() {
                        return updater.get(object);
                    }

                    @Override public T set(T newValue) {
                        return updater.set(object, newValue);
                    }

                    @Override public T updateTo(T newValue) {
                        return updater.updateTo(object, newValue);
                    }

                    @Override public T updateWith(Function<T, T> updatingFunction) {
                        return updater.updateWith(object, updatingFunction);
                    }

                    @Override
                    public T swapFor(T newValue) {
                        return updater.swapFor(object, newValue);
                    }

                    @Override
                    public T swapWith(Function<T, T> updatingFunction) {
                        return updater.swapWith(object, updatingFunction);
                    }
                    
                };
            }
        };
    }
    
    private Updaters() { }
    
}
