package com.timgroup.karg.reference;

import com.google.common.base.Function;

public interface Updater<O, T> extends Lens<O, T> {
    
    T updateTo(O object, T newValue);
    T updateWith(O object, Function<T, T> updater);
    T swapFor(O object, T newValue);
    T swapWith(O object, Function<T, T> updater);
    
}
