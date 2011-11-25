package com.timgroup.karg.multipledispatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.multipledispatch.CandidateFunction;

public class CandidateFunctionTest {
    
    private final Mockery context = new Mockery();
    
    @SuppressWarnings("unchecked")
    private final Predicate<KeywordArguments> predicate = context.mock(Predicate.class);
    
    @SuppressWarnings("unchecked")
    private final Function<KeywordArguments, String> function = context.mock(Function.class);
    
    @Test public void
    matchesUsingTheSuppliedPredicate() {
        CandidateFunction<String> candidate = new CandidateFunction<String>(predicate, null);
        
        final KeywordArguments parameterSet = KeywordArguments.of();
        
        context.checking(new Expectations() { {
            oneOf(predicate).apply(parameterSet); will(returnValue(true));
        } });
        
        assertThat(candidate.matches(parameterSet), is(true));
        context.assertIsSatisfied();
    }
    
    @Test public void
    appliesUsingTheSuppliedFunction() {
        CandidateFunction<String> candidate = new CandidateFunction<String>(null, function);
        
        final KeywordArguments parameterSet = KeywordArguments.of();
        
        context.checking(new Expectations() { {
            oneOf(function).apply(parameterSet); will(returnValue("value"));
        } });
        
        assertThat(candidate.apply(parameterSet), is("value"));
        context.assertIsSatisfied();
    }
}
