package com.timgroup.karg.reference;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;

public final class Lenses {
    
    public static interface LensObjectCapture<O, T> {
        Ref<T> to(O object);
    }

    public static <O, T> LensObjectCapture<O, T> bind(final Lens<O, T> lens) {
        return new LensObjectCapture<O, T>() {
            @Override public Ref<T> to(final O object) {
                return new Ref<T>() {
                    @Override public T get() {
                        return lens.get(object);
                    }

                    @Override public T set(T newValue) {
                        return lens.set(object, newValue);
                    }
                };
            }
        };
    }
    
    public static <T> Lens<List<T>, T> atIndex(final int index) {
        return new Lens<List<T>, T>() {
            @Override public T get(List<T> object) {
                return object.get(index);
            }

            @Override public T set(List<T> object, T newValue) {
                object.set(index, newValue);
                return newValue;
            }
        };
    }
    
    public static <T> Lens<List<T>, T> firstItem() {
        return atIndex(0);
    }

    public static <O, T> Predicate<O> compose(final Lens<O, T> lens, final Predicate<T> predicate) {
        return new Predicate<O>() {
            @Override public boolean apply(O arg0) {
                return predicate.apply(lens.get(arg0));
            }
        };
    }

    public static <O, T1, T2> Function<O, T2> compose(final Lens<O, T1> lens, final Function<T1, T2> function) {
        return Functions.compose(function, toFunction(lens));        
    }

    public static <O, T> Function<O, T> toFunction(final Lens<O, T> lens) {
        return new Function<O, T>() {
            @Override public T apply(O arg0) {
                return lens.get(arg0);
            }
        };
    }
    
    private Lenses() { }
    
}
