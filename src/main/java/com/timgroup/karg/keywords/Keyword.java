package com.timgroup.karg.keywords;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.timgroup.karg.reference.Lenses;
import com.timgroup.karg.reference.Ref;

public class Keyword<T> implements KeywordArgumentsLens<T> {
    
    public static final Keyword<String> NAME = Keyword.newKeyword();
    public static final Keyword<String> DISPLAY_NAME = Keyword.newKeyword();

    public static <T> Keyword<T> newKeyword() {
        return new Keyword<T>(KeywordArguments.of());
    }
    
    public static <T> Keyword<T> newKeyword(KeywordArgument...parameters) {
        return new Keyword<T>(KeywordArguments.of(parameters));
    }
    
    private final KeywordArguments metadata;
    
    private Keyword(KeywordArguments metadata) {
        this.metadata = metadata;
    }
    
    public KeywordArguments metadata() {
        return metadata;
    }
    
    public KeywordArgument of(T value) {
        return KeywordArgument.value(this, value);
    }

    public T from(KeywordArguments keywordArguments) {
        return from(keywordArguments, null);
    }
    
    public T from(KeywordArguments keywordArguments, T defaultValue) {
        T result = get(keywordArguments);
        return result == null ? defaultValue : result;
    }
    
    public Function<T, KeywordArguments> lift(final KeywordArgument...remainingArguments) {
        return new Function<T, KeywordArguments>() {
            @Override
            public KeywordArguments apply(T curriedArgument) {
                return KeywordArguments.of(Keyword.this.of(curriedArgument)).with(remainingArguments);
            }
        };
    }
    
    public <T2> Function<T, T2> lower(Function<KeywordArguments, T2> function, KeywordArgument...remainingArguments) {
        return Functions.compose(function, lift(remainingArguments));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(KeywordArguments object) {
        KeywordArgument arg = object.get(this);
        if (arg == null) { return null; }
        return (T) arg.value();
    }

    @Override
    public T set(KeywordArguments object, T newValue) {
        object.put(this, this.of(newValue));
        return newValue;
    }
    
    public Ref<T> bindTo(KeywordArguments object) {
        return Lenses.bind(this).to(object);
    }
}
