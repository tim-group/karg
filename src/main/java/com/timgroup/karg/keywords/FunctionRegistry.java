package com.timgroup.karg.keywords;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public interface FunctionRegistry<T> {
    
    public static interface MatchBuilder<T> {
        void with(Function<KeywordArguments, T> function);
    }
    
    MatchBuilder<T> match(Predicate<KeywordArguments> predicate);
    
}
