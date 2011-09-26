package com.timgroup.karg.reference;

import com.google.common.base.Function;

public interface Updatable<T> extends Ref<T> {

    T updateTo(T newValue);
    T updateWith(Function<T, T> updater);
    T swapFor(T newValue);
    T swapWith(Function<T, T> updater);
    
}
