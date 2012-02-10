package com.timgroup.karg.keywords.typed;

import com.timgroup.karg.keywords.KeywordArguments;

public interface TypedKeyword<O, T> extends TypedKeywordArgumentsLens<O, T> {

    KeywordArguments metadata();
    TypedKeywordArgument<O> of(T value);
    T from(TypedKeywordArguments<O> keywordArguments);
    T from(TypedKeywordArguments<O> keywordArguments, T defaultValue);
    
}
