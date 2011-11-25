package com.timgroup.karg.functions;

import com.google.common.base.Function;
import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;

public final class VarArgAdapter {
    
    private VarArgAdapter() { }
    
    public static <T> VarArgFunction<T> adapt(final Function<KeywordArguments, T> function) {
        return new VarArgFunction<T>() {
            @Override
            public T apply(KeywordArgument... parameters) {
                return function.apply(KeywordArguments.of(parameters));
            }
        };
    }

}
