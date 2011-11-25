package com.timgroup.karg.keywords;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.timgroup.karg.functions.KeywordFunction;
import com.timgroup.karg.multipledispatch.CandidateFunctionRegistry;

public class CandidateFunctionRegistryTest {

    private final Mockery context = new Mockery();
    
    @SuppressWarnings("unchecked")
    private final KeywordFunction<String> function = context.mock(KeywordFunction.class);
    
    @SuppressWarnings("unchecked")
    private final Predicate<KeywordArguments> predicate = context.mock(Predicate.class);
    
    @Test public void
    registers_a_function_bound_to_a_predicate() {
        final KeywordArguments arguments = KeywordArguments.empty();
        CandidateFunctionRegistry<String> registry = new CandidateFunctionRegistry<String>();
        
        registry.match(predicate).with(function);
        
        context.checking(new Expectations() {{
            allowing(predicate).apply(arguments); will(returnValue(true));
            oneOf(function).apply(arguments); will(returnValue("value"));
        }});
        
        registry.findCandidateMatching(arguments).apply(arguments);
        context.assertIsSatisfied();
    }
    

}
