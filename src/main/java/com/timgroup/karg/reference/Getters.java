package com.timgroup.karg.reference;

import com.google.common.base.Function;

public final class Getters<O, T> {
    
    public static interface GetterObjectCapture<O, T> {
        Gettable<T> to(O object);
    }
    
    public static <O, T> GetterObjectCapture<O, T> bind(final Getter<O, T> getter) {
        return new GetterObjectCapture<O, T>() {
            @Override public Gettable<T> to(final O object) {
                return new Gettable<T>() {
                    @Override public T get() {
                        return getter.get(object);
                    }
                };
            }
        };
    }
    
    public static <O, T> Function<O, T> toFunction(final Getter<O, T> getter) {
        return new Function<O, T>() {
            @Override public T apply(O object) {
                return getter.get(object);
            }
        };
    }
    
    public static <O, T> Getter<O, T> toGetter(final Function<O, T> function) {
        return new Getter<O, T>() {
            @Override public T get(O object) {
                return function.apply(object);
            }
        };
    }
    
    public static <O, T1, T2> Getter<O, T2> compose(final Getter<O, T1> first, final Getter<T1, T2> second) {
        return new Getter<O, T2>() {
            @Override public T2 get(O object) {
                return second.get(first.get(object));
            }
        };
    }
    
    public static <O, T1, T2> Lens<O, T2> compose(final Getter<O, T1> first, final Lens<T1, T2> second) {
        return new Lens<O, T2>() {
            @Override public T2 get(O object) {
                return second.get(first.get(object));
            }

            @Override
            public T2 set(O object, T2 newValue) {
                return second.set(first.get(object), newValue);
            }
        };
    }
    
    public static <O, T1, T2> Setter<O, T2> compose(final Getter<O, T1> first, final Setter<T1, T2> second) {
        return new Setter<O, T2>() {
            @Override
            public T2 set(O object, T2 newValue) {
                return second.set(first.get(object), newValue);
            }
        };
    }
    
    public Getters() { }

}
