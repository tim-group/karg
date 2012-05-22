package com.timgroup.karg.naming;

import java.util.Iterator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import static com.google.common.base.Functions.compose;
import static com.google.common.collect.Iterators.concat;
import static com.google.common.collect.Iterators.singletonIterator;
import static com.google.common.collect.Iterators.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.timgroup.karg.naming.StringFunctions.CAPITALISE;
import static com.timgroup.karg.naming.StringFunctions.LOWERCASE_TRIM;

public interface TargetNameFormatter {
    
    static final TargetNameFormatter LOWER_CAMEL_CASE = new LowerCamelCaseFormatter();
    static final TargetNameFormatter UPPER_CAMEL_CASE = new UpperCamelCaseFormatter();
    static final TargetNameFormatter UNDERSCORE_SEPARATED = new UnderscoreSeparatedFormatter();
    
    static final class LowerCamelCaseFormatter implements TargetNameFormatter {
        
        @Override
        public String format(Iterable<String> words) {
            Iterator<String> lowercased = transform(words.iterator(), LOWERCASE_TRIM);
            if (!lowercased.hasNext()) {
                return "";
            }
            Iterator<String> lowerCamelCased = concat(singletonIterator(lowercased.next()),
                                                      transform(lowercased, CAPITALISE));
            return Joiner.on("").join(Lists.newArrayList(lowerCamelCased));
        }
    }
    
    static final class UpperCamelCaseFormatter implements TargetNameFormatter {
        @Override
        public String format(Iterable<String> words) {
            Iterator<String> upperCamelCased = transform(words.iterator(),
                                                         compose(CAPITALISE, LOWERCASE_TRIM));
            return Joiner.on("").join(Lists.newArrayList(upperCamelCased));
        }
    }
    
    static final class UnderscoreSeparatedFormatter implements TargetNameFormatter {
        @Override
        public String format(Iterable<String> words) {
            Iterator<String> upperCamelCased = transform(words.iterator(), LOWERCASE_TRIM);
            return Joiner.on("_").join(newArrayList(upperCamelCased));
        }
    }
    
    public abstract String format(Iterable<String> words);
    
}
