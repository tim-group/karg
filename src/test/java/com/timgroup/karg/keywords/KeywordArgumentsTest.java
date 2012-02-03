package com.timgroup.karg.keywords;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;

import static com.timgroup.karg.keywords.Keywords.newKeyword;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class KeywordArgumentsTest {

    @Test public void
    hasStaticConstructorTakingArrayOfBoundParameters() {
        Keyword<String> param1 = newKeyword();
        Keyword<String> param2 = newKeyword();
        
        KeywordArgument bp1 = param1.of("Hello");
        KeywordArgument bp2 = param2.of("World");
        
        KeywordArguments ps = KeywordArguments.of(new KeywordArgument[] { bp1, bp2 });
        
        MatcherAssert.assertThat(ps, notNullValue(KeywordArguments.class));
    }
    
    @Test public void
    matchesASetOfParameters() {
        Keyword<String> param1 = newKeyword();
        Keyword<String> param2 = newKeyword();
        Keyword<String> param3 = newKeyword();
        
        KeywordArgument bp1 = param1.of("Hello");
        KeywordArgument bp2 = param2.of("World");
        
        KeywordArguments ps = KeywordArguments.of(new KeywordArgument[] { bp1, bp2 });
        
        MatcherAssert.assertThat(ps.matchesKeywords(Sets.<Keyword<?>>newHashSet(param1, param2)), is(true));
        MatcherAssert.assertThat(ps.matchesKeywords(Sets.<Keyword<?>>newHashSet(param2, param3)), is(false));
    }
    
    @Test public void
    returnsBoundValuesByParameter() {
        Keyword<String> param1 = newKeyword();
        Keyword<String> param2 = newKeyword();
        
        KeywordArgument bp1 = param1.of("Hello");
        KeywordArgument bp2 = param2.of("World");
        
        KeywordArguments ps = KeywordArguments.of(new KeywordArgument[] { bp1, bp2 });
        
        MatcherAssert.assertThat(ps.valueOf(param2), is("World"));
        MatcherAssert.assertThat(ps.valueOf(param1), is("Hello"));
    }
    
    @Test public void
    returnsNullIfParameterMissing() {
        Keyword<String> param1 = newKeyword();
        Keyword<String> param2 = newKeyword();
        Keyword<String> param3 = newKeyword();
        
        KeywordArgument bp1 = param1.of("Hello");
        KeywordArgument bp2 = param2.of("World");
        
        KeywordArguments ps = KeywordArguments.of(new KeywordArgument[] { bp1, bp2 });
        
        MatcherAssert.assertThat(ps.valueOf(param3), Matchers.nullValue());
    }
    
    @Test public void
    canBeExtendedWithFurtherParameters() {
        Keyword<String> param1 = newKeyword();
        Keyword<String> param2 = newKeyword();
        
        KeywordArgument bp1 = param1.of("Hello");
        
        KeywordArguments ps = KeywordArguments.of(new KeywordArgument[] { bp1 }).with(param2.of("World"));
        
        MatcherAssert.assertThat(ps.valueOf(param2), is("World"));
        MatcherAssert.assertThat(ps.valueOf(param1), is("Hello"));
    }
    
    @Test public void
    hasStaticMethodToConstructAPredicateMatchingOnContainedParameters() {
        Keyword<String> param1 = newKeyword();
        Keyword<String> param2 = newKeyword();
        Keyword<String> param3 = newKeyword();
        
        KeywordArgument bp1 = param1.of("Goodbye");
        KeywordArgument bp2 = param2.of("Cruel");
        KeywordArgument bp3 = param3.of("World");
        
        KeywordArguments ps1 = KeywordArguments.of(new KeywordArgument[] { bp1, bp2 });
        KeywordArguments ps2 = KeywordArguments.of(new KeywordArgument[] { bp2, bp3 });
        KeywordArguments ps3 = KeywordArguments.of(new KeywordArgument[] { bp1, bp2, bp3 });
        
        Predicate<KeywordArguments> predicate = KeywordArguments.containing(param2, param3);
        MatcherAssert.assertThat(predicate.apply(ps1), Matchers.is(false));
        MatcherAssert.assertThat(predicate.apply(ps2), Matchers.is(true));
        MatcherAssert.assertThat(predicate.apply(ps3), Matchers.is(false));
    }
}
