package com.timgroup.karg.functions;

import org.junit.Test;

import com.google.common.base.Function;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.keywords.Keywords;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class VarArgAdapterTest {
    
    @Test public void
    adapts_a_parameter_set_function_to_a_var_arg_function() {
        final Keyword<String> name = Keywords.newKeyword();
        
        Function<KeywordArguments, String> unadapted = new Function<KeywordArguments, String>() {
            @Override public String apply(KeywordArguments input) {
                return String.format("Hello, %s", name.from(input));
            }
        };
        
        VarArgFunction<String> adapted = VarArgAdapter.adapt(unadapted);
        
        assertThat(adapted.apply(name.of("Dominic")), is("Hello, Dominic"));
    }
    

}
