package com.timgroup.karg.reference;

import com.google.common.base.Function;

public final class Settables {
    
    private Settables() {}

    public static <T> Function<T, T> toFunction(final Settable<T> setter) {
        return new Function<T, T>() {
            @Override public T apply(T newValue) {
                return setter.set(newValue);
            }
        };
    }

}
 