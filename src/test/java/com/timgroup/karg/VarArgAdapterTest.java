package com.timgroup.karg;

import org.junit.Test;

import com.google.common.base.Function;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.keywords.VarArgAdapter;
import com.timgroup.karg.keywords.VarArgFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class VarArgAdapterTest {
    
    @Test public void
    adaptsAParameterSetFunctionToAVarArgFunction() {
        final Keyword<String> name = Keyword.newKeyword();
        
        Function<KeywordArguments, String> unadapted = new Function<KeywordArguments, String>() {
            @Override public String apply(KeywordArguments input) {
                return String.format("Hello, %s", name.from(input));
            }
        };
        
        VarArgFunction<String> adapted = VarArgAdapter.adapt(unadapted);
        
        assertThat(adapted.apply(name.of("Dominic")), is("Hello, Dominic"));
    }
    

}
