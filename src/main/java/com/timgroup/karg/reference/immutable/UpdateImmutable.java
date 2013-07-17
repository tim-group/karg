package com.timgroup.karg.reference.immutable;

import com.google.common.base.Function;

public final class UpdateImmutable {
    
    public static interface UpdateCapture<O, T> {
        O with(Function<T, T> updater);
        O to(T newValue);
    }
    
    public static interface TargetCapture<O, T> {
        UpdateCapture<O, T> of(O object);
    }
    
    private UpdateImmutable() { }
    
    public static <O, T> UpdateCapture<O, T> the(final ImmutableRef<O, T> ref) {
        return new UpdateCapture<O, T>() {
            @Override public O with(Function<T, T> updater) {
                return ref.update(updater);
            }
            @Override public O to(T newValue) {
                return ref.set(newValue);
            }
        };
    }
    
    public static <O, T> TargetCapture<O, T> the(final ImmutableLens<O, T> updater) {
        return new TargetCapture<O, T>() {
            @Override public UpdateCapture<O, T> of(final O object) {
                return the(ImmutableLenses.bind(updater).to(object));
            }
        };
    }
}
