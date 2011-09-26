package com.timgroup.karg.reference;

import com.google.common.base.Function;

public final class Update {
    
    public static interface UpdateCapture<T> {
        T with(Function<T, T> updater);
        T to(T newValue);
    }
    
    public static interface TargetCapture<O, T> {
        UpdateCapture<T> of(O object);
    }
    
    private Update() { }
    
    public static <T> UpdateCapture<T> the(final Updatable<T> ref) {
        return new UpdateCapture<T>() {
            @Override public T with(Function<T, T> updater) {
                return ref.updateWith(updater);
            }
            @Override public T to(T newValue) {
                return ref.updateTo(newValue);
            }
        };
    }
    
    public static <T> UpdateCapture<T> the(final Ref<T> ref) {
        return the(ConcurrentRef.to(ref));
    }
    
    public static <O, T> TargetCapture<O, T> the(final Updater<O, T> updater) {
        return new TargetCapture<O, T>() {
            @Override public UpdateCapture<T> of(final O object) {
                return the(Updaters.bind(updater).to(object));
            }
        };
    }
    
    public static <O, T> TargetCapture<O, T> the(final Lens<O, T> lens) {
        return new TargetCapture<O, T>() {
            @Override public UpdateCapture<T> of(final O object) {
                return the(Lenses.bind(lens).to(object));
            }
        };
    }

}
