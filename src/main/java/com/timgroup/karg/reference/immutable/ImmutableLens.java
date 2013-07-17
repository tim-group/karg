package com.timgroup.karg.reference.immutable;

import com.google.common.base.Function;
import com.timgroup.karg.reference.Getter;

public interface ImmutableLens<O, T> extends Getter<O, T>, ImmutableSetter<O, T> {
    O update(O object, Function<T, T> update);
}
