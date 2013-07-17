package com.timgroup.karg.valuetypes;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.timgroup.karg.keywords.typed.TypedKeyword;
import com.timgroup.karg.keywords.typed.TypedKeywordArguments;
import com.timgroup.karg.reference.immutable.ImmutableLens;
import com.timgroup.karg.reference.immutable.ImmutableLenses;

public final class ValueTypes {

    private ValueTypes() { }
    
    public static <O extends ValueType<O>> ImmutableLens<O, TypedKeywordArguments<O>> fieldsLens() {
        return new ImmutableLens<O, TypedKeywordArguments<O>>() {
            @Override
            public TypedKeywordArguments<O> get(O object) {
                return object.fields();
            }

            @Override
            public O set(O object,
                    TypedKeywordArguments<O> newValue) {
                return object.makeNew(newValue);
            }

            @Override
            public O update(
                    O object,
                    Function<TypedKeywordArguments<O>, TypedKeywordArguments<O>> update) {
                return set(object, update.apply(get(object)));
            }
        };
    }
    
    public static <O, T> ImmutableLens<TypedKeywordArguments<O>, T> toImmutableLens(final TypedKeyword<O, T> keyword) {
        return new ImmutableLens<TypedKeywordArguments<O>, T>() {
            @Override
            public T get(TypedKeywordArguments<O> args) {
                return keyword.from(args);
            }

            @Override
            public TypedKeywordArguments<O> set(TypedKeywordArguments<O> args, T newValue) {
                return args.with(keyword.of(newValue));
            }

            @Override
            public TypedKeywordArguments<O> update(
                    TypedKeywordArguments<O> object, Function<T, T> update) {
                return set(object, update.apply(get(object)));
            }
        };
    }
    
    public static <O extends ValueType<O>, T> ImmutableLens<O, T> toValueTypeLens(TypedKeyword<O, T> keyword) {
        ImmutableLens<O, TypedKeywordArguments<O>> fieldsLens = fieldsLens();
        return ImmutableLenses.compose(fieldsLens, toImmutableLens(keyword));
    }
    
    public static <O extends ValueType<O>, T> Function<O, T> project(final TypedKeyword<O, T> keyword) {
        return ImmutableLenses.project(toValueTypeLens(keyword));
    }
    
    public static <O extends ValueType<O>, T> Predicate<O> select(final TypedKeyword<O, T> keyword, final Predicate<T> predicate) {
        return ImmutableLenses.select(toValueTypeLens(keyword), predicate);
    }
    
    public static <O extends ValueType<O>, T> Predicate<O> select(final TypedKeyword<O, T> keyword, T value) {
        return ImmutableLenses.select(toValueTypeLens(keyword), value);
    }
    
    public static <O extends ValueType<O>, T> Function<O, O> update(final TypedKeyword<O, T> keyword, final Function<T, T> update) {
        return ImmutableLenses.update(toValueTypeLens(keyword), update);
    }
    
    public static <O extends ValueType<O>, T> Function<O, O> update(final TypedKeyword<O, T> keyword, T value) {
        return ImmutableLenses.update(toValueTypeLens(keyword), value);
    }
}
