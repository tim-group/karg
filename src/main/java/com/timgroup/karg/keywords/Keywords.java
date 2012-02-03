package com.timgroup.karg.keywords;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public final class Keywords {
    
    public static final Keyword<String> NAME = newKeyword();
    public static final Keyword<String> DISPLAY_NAME = newKeyword();
    
    private Keywords() { }
    
    public static <T> Keyword<T> newKeyword() {
        return new ActualKeyword<T>(KeywordArguments.of());
    }
    
    public static <T> Keyword<T> newKeyword(KeywordArgument...parameters) {
        return new ActualKeyword<T>(KeywordArguments.of(parameters));
    }
    
    public static <T> Function<T, KeywordArguments> lift(final Keyword<T> keyword, final KeywordArgument...remainingArguments) {
        return new Function<T, KeywordArguments>() {
            @Override
            public KeywordArguments apply(T curriedArgument) {
                return KeywordArguments.of(keyword.of(curriedArgument)).with(remainingArguments);
            }
        };
    }
    
    public static <T, T2> Function<T, T2> lower(Keyword<T> keyword, Function<KeywordArguments, T2> function, KeywordArgument...remainingArguments) {
        return Functions.compose(function, lift(keyword, remainingArguments));
    }
    
    private static final class ActualKeyword<T> implements Keyword<T> {
        private final KeywordArguments metadata;
        
        private ActualKeyword(KeywordArguments metadata) {
            this.metadata = metadata;
        }
        
        @Override
        public KeywordArguments metadata() {
            return metadata;
        }
        
        @Override
        public KeywordArgument of(T value) {
            return KeywordArgument.value(this, value);
        }

        @Override
        public T from(KeywordArguments keywordArguments) {
            return from(keywordArguments, null);
        }
        
        @Override
        public T from(KeywordArguments keywordArguments, T defaultValue) {
            T result = get(keywordArguments);
            return result == null ? defaultValue : result;
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
    }

}
