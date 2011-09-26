package com.timgroup.karg.keywords;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class ParameterMatching<T> implements KeywordFunction<T>, VarArgFunction<T> {

    private final List<CandidateFunction<T>> candidates = newArrayList();
    
    public static <T> ParameterMatching<T> function(FunctionBundle<T> bundle) {
        return new ParameterMatching<T>(bundle);
    }
    
    private ParameterMatching(FunctionBundle<T> bundle) {
        bundle.register(new FunctionRegistry<T>() {
            @Override
            public MatchBuilder<T> match(final Predicate<KeywordArguments> predicate) {
                return new MatchBuilder<T>() {
                    @Override
                    public void with(Function<KeywordArguments, T> function) {
                        candidates.add(new CandidateFunction<T>(predicate, function));
                    }
                };
            }
        });
    }
    
    @Override
    public T apply(KeywordArguments parameters) {
        for (CandidateFunction<T> candidate : candidates) {
            if (candidate.matches(parameters)) {
                return candidate.apply(parameters);
            }
        }
        throw new UnsupportedOperationException("No function found matching the supplied parameters.");
    }
    
    @Override
    public T apply(KeywordArgument...parameters) {
        return apply(KeywordArguments.of(parameters));
    }

}
