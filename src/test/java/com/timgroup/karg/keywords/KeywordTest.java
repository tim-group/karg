package com.timgroup.karg.keywords;

import java.util.Collection;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.timgroup.karg.functions.KeywordFunction;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.reference.Lenses;
import com.timgroup.karg.reference.Ref;

import static com.timgroup.karg.keywords.Keywords.newKeyword;

import static com.google.common.base.Functions.compose;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class KeywordTest {
    
    private static final Keyword<String> TEST_PARAM1 = newKeyword();
    private static final Keyword<String> TEST_PARAM2 = newKeyword();
    
    @Test public void
    hasStaticConstructorWhichInfersType() {
        Keyword<Integer> keyword = Keywords.newKeyword();
        assertThat(keyword, notNullValue(Keyword.class));
    }
    
    @SuppressWarnings("rawtypes")
    @Test public void
    bindsValueToKeywordArgument() {
        KeywordArgument arg1 = TEST_PARAM1.of("Hello");
        assertThat(arg1.keyword(), is((Keyword) TEST_PARAM1));
        assertThat((String) arg1.value(), is("Hello"));
    }
    
    @Test public void
    retrievesValueFromKeywordArgumentMap() {
        KeywordArgument arg1 = TEST_PARAM1.of("Hello");
        KeywordArguments keywardArguments = KeywordArguments.of(arg1);
        assertThat(TEST_PARAM1.from(keywardArguments), is("Hello"));
    }

    @Test public void
    retrievesSpecifiedDefaultValueWhenValueNotFoundInKeywordArgumentMap() {
        KeywordArgument arg1 = TEST_PARAM1.of("Hello");
        KeywordArguments keywordArguments = KeywordArguments.of(arg1);
        assertThat(TEST_PARAM2.from(keywordArguments, "Marvin"), is("Marvin"));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    providesFunctionToLiftASingleValueToAKeywordArgumentSet() {
        final Keyword<String> greeting = Keywords.newKeyword();
        final Keyword<String> name = Keywords.newKeyword();
        
        KeywordFunction<String> greet = new KeywordFunction<String>() {
            @Override public String apply(KeywordArguments input) {
                return format("%s, %s", greeting.from(input), name.from(input));
            }
        };
        
        List<String> names = newArrayList("Peter", "Paul", "Mary");
        Collection<String> greetings = transform(names, compose(greet, Keywords.lift(name, greeting.of("Hello"))));
        
        assertThat(greetings, Matchers.<String>hasItems(is("Hello, Peter"),
                                                is("Hello, Paul"),
                                                is("Hello, Mary")));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    canLowerAKeywordFunctionToAPlainValueFunction() {
        final Keyword<String> greeting = Keywords.newKeyword();
        final Keyword<String> name = Keywords.newKeyword();
        
        KeywordFunction<String> greet = new KeywordFunction<String>() {
            @Override public String apply(KeywordArguments input) {
                return format("%s, %s", greeting.from(input), name.from(input));
            }
        };
        
        List<String> names = newArrayList("Peter", "Paul", "Mary");
        Collection<String> greetings = transform(names, Keywords.lower(name, greet, greeting.of("Goodbye")));
        
        assertThat(greetings, Matchers.<String>hasItems(is("Goodbye, Peter"),
                                                is("Goodbye, Paul"),
                                                is("Goodbye, Mary")));
    }
    
    @Test public void
    hasArbitraryMetadata() {
        Keyword<Integer> MIN_VALUE = Keywords.newKeyword();
        Keyword<Integer> MAX_VALUE = Keywords.newKeyword();
        Keyword<Integer> BOUNDED_VALUE = Keywords.newKeyword(MIN_VALUE.of(0),
                                                            MAX_VALUE.of(100));
        
        assertThat(BOUNDED_VALUE.metadata().valueOf(MIN_VALUE), is(0));
        assertThat(BOUNDED_VALUE.metadata().valueOf(MAX_VALUE), is(100));
    }
    
    @Test public void
    isLensIntoKeywordArguments() {
        Keyword<String> greeting = Keywords.newKeyword();
        Keyword<String> name = Keywords.newKeyword();
        
        KeywordArguments args1 = KeywordArguments.of(greeting.of("Hello"), name.of("World"));
        assertThat(greeting.get(args1), is("Hello"));
        
        greeting.set(args1, "Goodbye");
        assertThat(args1.valueOf(greeting), is("Goodbye"));
    }
    
    @Test public void
    bindsToKeywordArgumentsToMakeRef() {
        Keyword<String> greeting = Keywords.newKeyword();
        Keyword<String> name = Keywords.newKeyword();
        
        KeywordArguments args1 = KeywordArguments.of(greeting.of("Hello"), name.of("World"));
        
        Ref<String> maybeName = Lenses.bind(name).to(args1);
        
        assertThat(maybeName.get(), is("World"));
        maybeName.set("Sailor");
        assertThat(name.get(args1), is("Sailor"));
    }
    
}
