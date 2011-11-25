package com.timgroup.karg.multipledispatch;

import com.timgroup.karg.functions.KeywordFunction;
import com.timgroup.karg.functions.VarArgFunction;
import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;




public class ParameterMatching<T> implements KeywordFunction<T>, VarArgFunction<T> {

    private final CandidateFunctionRegistry<T> candidateRegistry = new CandidateFunctionRegistry<T>();
    
    public static <T> ParameterMatching<T> function(FunctionBundle<T> bundle) {
        return new ParameterMatching<T>(bundle);
    }
    
    private ParameterMatching(FunctionBundle<T> bundle) {
        bundle.register(candidateRegistry);
    }
    
    @Override
    public T apply(KeywordArguments parameters) {
        return candidateRegistry.findCandidateMatching(parameters).apply(parameters);
    }
    
    @Override
    public T apply(KeywordArgument...parameters) {
        return apply(KeywordArguments.of(parameters));
    }

}
