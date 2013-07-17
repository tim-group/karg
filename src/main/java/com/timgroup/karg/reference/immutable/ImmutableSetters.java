package com.timgroup.karg.reference.immutable;

public class ImmutableSetters {

    public static interface SetterObjectCapture<O, T> {
        ImmutableSettable<O, T> to(O object);
    }
    
    public static <O, T> SetterObjectCapture<O, T> bind(final ImmutableSetter<O, T> setter) {
        return new SetterObjectCapture<O, T>() {
            @Override public ImmutableSettable<O, T> to(final O object) {
                return new ImmutableSettable<O, T>() {
                    @Override public O set(T newValue) {
                        return setter.set(object, newValue);
                    }
                };
            }
        };
    }

    private ImmutableSetters() { }
}
