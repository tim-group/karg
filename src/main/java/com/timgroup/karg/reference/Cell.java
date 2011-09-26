package com.timgroup.karg.reference;

public final class Cell<T> implements Ref<T> {

    public static <T> Updatable<T> of(final T value) {
        return ConcurrentRef.to(new Cell<T>(value));
    }
    
    private T value;
    
    private Cell(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public T set(T newValue) {
        value = newValue;
        return newValue;
    }
}
