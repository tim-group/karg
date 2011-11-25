package com.timgroup.karg.multipledispatch;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.multipledispatch.CandidateFunctionRegistry;
import com.timgroup.karg.multipledispatch.FunctionBundle;
import com.timgroup.karg.multipledispatch.ParameterMatching;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;


public class ParameterMatchingTest {
    
    private static final Keyword<String> title = Keyword.newKeyword();
    private static final Keyword<String> firstName = Keyword.newKeyword();
    private static final Keyword<String> lastName = Keyword.newKeyword();
    
    @Test public void
    findsMatchingMultiFunctionBasedOnParameterSet() {
        
        final Function<KeywordArguments, String> sayHelloInformally = new Function<KeywordArguments, String>() {
            @Override public String apply(KeywordArguments parameters) {
                return format("Hello %s", firstName.from(parameters));
            }
        };
        
        final Function<KeywordArguments, String> sayHelloFormally = new Function<KeywordArguments, String>() {
            @Override public String apply(KeywordArguments parameters) {
                return format("Hello %s %s", title.from(parameters), lastName.from(parameters));
            }
        };
        
        final FunctionBundle<String> bundle = new FunctionBundle<String>() {
            @Override
            public void register(CandidateFunctionRegistry<String> registry) {
                registry.match(KeywordArguments.containing(firstName)).with(sayHelloInformally);
                registry.match(KeywordArguments.containing(title, lastName)).with(sayHelloFormally);        
            }
        };
        
        ParameterMatching<String> addressAppropriately = ParameterMatching.function(bundle);
        
        MatcherAssert.assertThat(addressAppropriately.apply(firstName.of("Dave")), is("Hello Dave"));
        MatcherAssert.assertThat(addressAppropriately.apply(title.of("Mr"), lastName.of("Bowman")), is("Hello Mr Bowman"));
    }
    
    @Test(expected=UnsupportedOperationException.class) public void
    throwsExceptionIfNoMatchingFunctionFound() {
        final FunctionBundle<String> emptyBundle = new FunctionBundle<String>() {
            @Override public void register(CandidateFunctionRegistry<String> registry) { }
        };
        
        ParameterMatching<String> addressAppropriately = ParameterMatching.function(emptyBundle);
        addressAppropriately.apply(firstName.of("Dave"));
    }

    @Test public void
    findsMatchingFunctionBasedOnMatcherPredicate() {
        
        final Keyword<Integer> value = Keyword.newKeyword();
        
        final Function<KeywordArguments, String> greaterThanOrEqualToZero = new Function<KeywordArguments, String>() {
            @Override public String apply(KeywordArguments parameters) {
                return format("The value %d is greater than or equal to zero.", value.from(parameters));
            }
        };
        
        final Function<KeywordArguments, String> lessThanZero = new Function<KeywordArguments, String>() {
            @Override public String apply(KeywordArguments parameters) {
                return format("The value %d is less than zero.", value.from(parameters));
            }
        };
        
        final Predicate<KeywordArguments> valueGreaterThanOrEqualToZero = new Predicate<KeywordArguments>() {
            @Override
            public boolean apply(KeywordArguments input) {
                return value.from(input) >= 0;
            }
        };
        
        final FunctionBundle<String> bundle = new FunctionBundle<String>() {
            @Override
            public void register(CandidateFunctionRegistry<String> registry) {
                registry.match(valueGreaterThanOrEqualToZero).with(greaterThanOrEqualToZero);
                registry.match(Predicates.not(valueGreaterThanOrEqualToZero)).with(lessThanZero);        
            }
        };
        
        ParameterMatching<String> reportOnParameter = ParameterMatching.function(bundle);
        
        MatcherAssert.assertThat(reportOnParameter.apply(value.of(2)), is("The value 2 is greater than or equal to zero."));
        MatcherAssert.assertThat(reportOnParameter.apply(value.of(-3)), is("The value -3 is less than zero."));
    }
    
}
