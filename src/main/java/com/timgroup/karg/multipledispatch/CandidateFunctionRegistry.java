package com.timgroup.karg.multipledispatch;

import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.timgroup.karg.keywords.KeywordArguments;

import static com.google.common.collect.Lists.newArrayList;

public class CandidateFunctionRegistry<T> {
    
    private final List<CandidateFunction<T>> candidates = newArrayList();
    
    public static interface MatchBuilder<T> {
        void with(Function<KeywordArguments, T> function);
    }
    
    public MatchBuilder<T> match(final Predicate<KeywordArguments> predicate) {
        return new MatchBuilder<T>() {
            @Override
            public void with(Function<KeywordArguments, T> function) {
                candidates.add(new CandidateFunction<T>(predicate, function));
            }
        };
    }
    
    public CandidateFunction<T> findCandidateMatching(KeywordArguments parameters) {
        try {
            return Iterables.find(candidates, matchesParameters(parameters));
        } catch (NoSuchElementException e) {
            throw new UnsupportedOperationException("No candidate function was found matching the supplied parameters.");
        }
    }
    
    private Predicate<CandidateFunction<T>> matchesParameters(final KeywordArguments parameters) {
        return new Predicate<CandidateFunction<T>>() {
            @Override public boolean apply(CandidateFunction<T> candidate) {
                return candidate.matches(parameters);
            }
        };
    }
}