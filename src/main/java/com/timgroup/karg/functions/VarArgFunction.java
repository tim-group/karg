package com.timgroup.karg.functions;

import com.timgroup.karg.keywords.KeywordArgument;


public interface VarArgFunction<T> {
    
    T apply(KeywordArgument...parameters);
    
}
