package com.timgroup.karg.reference.immutable;

public interface ImmutableSetter<O, T> {

    O set(O object, T newValue);
    
}
