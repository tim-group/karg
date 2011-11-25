package com.timgroup.karg.keywords;

import org.junit.Test;

import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArgument;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class KeywordArgumentTest {

    @SuppressWarnings("unchecked")
    @Test public void
    bindsAKeywordToAValue() {
        Keyword<String> name = Keyword.newKeyword();
        KeywordArgument bound = KeywordArgument.value(name, "Hello");
        assertThat((Keyword<String>) bound.keyword(), is(name));
        assertThat((String) bound.value(), is("Hello"));
    }
    
}
