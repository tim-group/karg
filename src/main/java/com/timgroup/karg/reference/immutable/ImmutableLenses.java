package com.timgroup.karg.reference.immutable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public final class ImmutableLenses {

    private ImmutableLenses() { }
    
    public static <O, T1, T2> ImmutableLens<O, T2> compose(final ImmutableLens<O, T1> lens1, final ImmutableLens<T1, T2> lens2) {
        return new ImmutableLens<O, T2>() {
            @Override
            public T2 get(O object) {
                return lens2.get(lens1.get(object));
            }

            @Override
            public O set(O object, T2 newValue) {
                return lens1.set(object, lens2.set(lens1.get(object), newValue));
            }

            @Override
            public O update(O object, Function<T2, T2> update) {
                return lens1.set(object,  lens2.update(lens1.get(object), update));
            }
        };
    }
    
    public static interface ImmutableLensObjectCapture<O, T> {
        ImmutableRef<O, T> to(O object);
    }
    
    public static <O, T> ImmutableLensObjectCapture<O, T> bind(final ImmutableLens<O, T> lens) {
        return new ImmutableLensObjectCapture<O, T>() {
            @Override
            public ImmutableRef<O, T> to(final O object) {
                return new ImmutableRef<O, T>() {
                    @Override
                    public T get() {
                        return lens.get(object);
                    }

                    @Override
                    public O set(T newValue) {
                        return lens.set(object, newValue);
                    }

                    @Override
                    public O update(Function<T, T> updater) {
                        return lens.set(object, updater.apply(get()));
                    }
                };
            }
        };
    }
    
    public static <O, T> Function<O, T> toFunction(final ImmutableLens<O, T> lens) {
        return new Function<O, T>() {
            @Override public T apply(O arg0) {
                return lens.get(arg0);
            }
        };
    }
    
    public static <O, T> Function<O, T> project(final ImmutableLens<O, T> lens) {
        return new Function<O, T>() {
            @Override
            public T apply(O input) {
                return lens.get(input);
            }
        };
    }
    
    public static <O, T> Predicate<O> select(final ImmutableLens<O, T> lens, final Predicate<T> predicate) {
        return new Predicate<O>() {
            public boolean apply(O input) {
                return predicate.apply(lens.get(input));
            }
        };
    }
    
    public static <O, T> Predicate<O> select(final ImmutableLens<O, T> lens, T value) {
        return select(lens, Predicates.equalTo(value));
    }
    
    public static <O, T> Function<O, O> update(final ImmutableLens<O, T> lens, final Function<T, T> update) {
        return new Function<O, O>() {
            @Override
            public O apply(O input) {
                return lens.update(input, update);
            }
        };
    }
    
    public static <O, T> Function<O, O> update(final ImmutableLens<O, T> lens, final T value) {
        return new Function<O, O>() {
            @Override
            public O apply(O input) {
                return lens.set(input, value);
            }
        };
    }
}
