package com.timgroup.karg.reference.immutable;

import com.timgroup.karg.reference.Gettable;
import com.google.common.base.Function;

public interface ImmutableRef<O, T> extends Gettable<T>, ImmutableSettable<O, T> {
    
    O update(Function<T, T> updater);
    
}
