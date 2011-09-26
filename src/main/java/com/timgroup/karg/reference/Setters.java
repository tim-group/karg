package com.timgroup.karg.reference;



public final class Setters {
    
    public static interface SetterObjectCapture<O, T> {
        Settable<T> to(O object);
    }
    
    public static <O, T> SetterObjectCapture<O, T> bind(final Setter<O, T> setter) {
        return new SetterObjectCapture<O, T>() {
            @Override public Settable<T> to(final O object) {
                return new Settable<T>() {
                    @Override public T set(T newValue) {
                        return setter.set(object, newValue);
                    }
                };
            }
        };
    }

    private Setters() { }
}
