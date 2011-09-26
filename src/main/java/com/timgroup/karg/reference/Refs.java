package com.timgroup.karg.reference;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public final class Refs {
    
    public static <T> Iterable<T> toIterable(final Ref<T> ref) {
        return new Iterable<T>() {
            @Override public Iterator<T> iterator() {
                return new Iterator<T>() {
                    private boolean consumed = ref.get() == null;
                    @Override public boolean hasNext() {
                        return !consumed;
                    }
    
                    @Override public T next() {
                        if (consumed) {
                            return null;
                        }
                        consumed = true;
                        return ref.get();
                    }
    
                    @Override public void remove() {
                        ref.set(null);
                    }
                };
            }
        };
    }
    
    public static <T> Function<Ref<T>, Iterable<T>> toIterable() {
        return new Function<Ref<T>, Iterable<T>>() {
            @Override public Iterable<T> apply(Ref<T> arg0) {
                return toIterable(arg0);
            }
        };
    }
    
    public static <T> Iterable<T> toIterable(Collection<? extends Ref<T>> refs) {
        Function<Ref<T>, Iterable<T>> toIterable = toIterable();
        return Iterables.concat(Iterables.transform(refs, toIterable));
    }
    
    private Refs() {  }
    
}
