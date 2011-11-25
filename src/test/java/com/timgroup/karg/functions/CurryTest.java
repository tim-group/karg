package com.timgroup.karg.functions;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.timgroup.karg.functions.Curry;
import com.timgroup.karg.functions.KeywordFunction;
import com.timgroup.karg.functions.VarArgAdapter;
import com.timgroup.karg.functions.VarArgFunction;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArguments;


public class CurryTest {
    
    private static final Keyword<String> greeting = Keyword.newKeyword();
    private static final Keyword<String> addressee = Keyword.newKeyword();
    
    private final class GreetFunction implements KeywordFunction<String> {
        @Override
        public String apply(KeywordArguments parameters) {
            return format("%s %s", greeting.from(parameters), addressee.from(parameters));
        }
    }
    
    @Test public void
    appliesDefaultParametersAutomatically() {
        VarArgFunction<String> greet = VarArgAdapter.adapt(Curry.theFunction(new GreetFunction()).with(greeting.of("Goodbye")));
        
        MatcherAssert.assertThat(greet.apply(addressee.of("World")), is("Goodbye World"));
    }
    
    @Test public void
    suppliedParametersOverrideDefaultParameters() {
        VarArgFunction<String> greet = VarArgAdapter.adapt(Curry.theFunction(new GreetFunction()).with(greeting.of("Goodbye")));
        
        MatcherAssert.assertThat(greet.apply(greeting.of("Ciao"), addressee.of("World")), is("Ciao World"));
    }
}
