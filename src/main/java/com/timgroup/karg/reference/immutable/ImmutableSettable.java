package com.timgroup.karg.reference.immutable;

public interface ImmutableSettable<O, T> {

    O set(T newValue);
    
}
