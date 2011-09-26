package com.timgroup.karg.reference;

public interface Setter<O, T> {
    T set(O object, T newValue);
}
