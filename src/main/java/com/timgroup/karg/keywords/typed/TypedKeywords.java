package com.timgroup.karg.keywords.typed;

import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;

public class TypedKeywords {
    
    public static <O, T> TypedKeyword<O, T> newTypedKeyword() {
        return new ActualTypedKeyword<O, T>(KeywordArguments.empty());
    }
    
    public static <O, T> TypedKeyword<O, T> newTypedKeyword(KeywordArgument...arguments) {
        return newTypedKeyword(KeywordArguments.of(arguments));
    }
    
    public static <O, T> TypedKeyword<O, T> newTypedKeyword(KeywordArguments arguments) {
        return new ActualTypedKeyword<O, T>(arguments);
    }
    
    private static final class ActualTypedKeyword<O, T> implements TypedKeyword<O, T> {

        private final KeywordArguments metadata;
        
        public ActualTypedKeyword(KeywordArguments metadata) {
            this.metadata = metadata;
        }
        
        @Override
        public KeywordArguments metadata() { 
            return metadata;
        }

        @Override
        public TypedKeywordArgument<O> of(T value) {
            return TypedKeywordArgument.value(this, value);
        }

        @Override
        public T from(TypedKeywordArguments<O> keywordArguments) {
            return from(keywordArguments, null);
        }
        
        @Override
        public T from(TypedKeywordArguments<O> keywordArguments, T defaultValue) {
            T result = get(keywordArguments);
            return result == null ? defaultValue : result;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T get(TypedKeywordArguments<O> arguments) {
            TypedKeywordArgument<O> argument = arguments.get(this);
            return argument == null ? null : (T) argument.value();
        }

        @Override
        public T set(TypedKeywordArguments<O> arguments, T newValue) {
            arguments.put(this, this.of(newValue));
            return newValue;
        }
    }

}
