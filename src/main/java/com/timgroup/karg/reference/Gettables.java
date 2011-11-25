package com.timgroup.karg.reference;

import java.util.concurrent.Callable;

import com.google.common.base.Function;

public final class Gettables {

    private Gettables() {
    }

    public static <T> Callable<T> toCallable(final Gettable<T> gettable) {
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                return gettable.get();
            }
        };
    }

    public static <T1, T2> Gettable<T2> compose(final Gettable<T1> gettable, final Getter<T1, T2> getter) {
        return new Gettable<T2>() {
            @Override
            public T2 get() {
                T1 intermediate = gettable.get();
                return getter.get(intermediate);
            }
        };
    }

    public static <T1, T2> Settable<T2> compose(final Gettable<T1> gettable, final Setter<T1, T2> setter) {
        return new Settable<T2>() {
            @Override
            public T2 set(T2 newValue) {
                T1 intermediate = gettable.get();
                return setter.set(intermediate, newValue);
            }
        };
    }

    public static <T1, T2> Ref<T2> compose(final Gettable<T1> gettable, final Lens<T1, T2> lens) {
        return new Ref<T2>() {
            @Override
            public T2 get() {
                return lens.get(gettable.get());
            }

            @Override
            public T2 set(T2 newValue) {
                return lens.set(gettable.get(), newValue);
            }
        };
    }

    public static <T1, T2> Gettable<T2> compose(final Gettable<T1> gettable, final Function<T1, T2> function) {
        return compose(gettable, Getters.toGetter(function));
    }
}
