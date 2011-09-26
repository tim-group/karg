package com.timgroup.karg.keywords;

public interface VarArgFunction<T> {
    
    T apply(KeywordArgument...parameters);
    
}
