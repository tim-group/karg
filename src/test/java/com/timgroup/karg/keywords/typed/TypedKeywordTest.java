package com.timgroup.karg.keywords.typed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class TypedKeywordTest {

    @Test public void
    type_bound_keywords_create_typed_keyword_arguments() {
        TypedKeyword<TypedKeywordTest, String> name = TypedKeywords.newTypedKeyword();
        TypedKeyword<TypedKeywordTest, Integer> age = TypedKeywords.newTypedKeyword();
        
        TypedKeywordArguments<TypedKeywordTest> args = TypedKeywordArguments.of(name.of("Dominic")).with(age.of(37));
        
        assertThat(name.from(args), is("Dominic"));
        assertThat(age.from(args), is(37));
    }
}
