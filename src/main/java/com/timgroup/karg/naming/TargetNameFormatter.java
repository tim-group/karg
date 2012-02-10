package com.timgroup.karg.naming;

import static com.google.common.base.Functions.compose;
import static com.google.common.collect.Iterators.concat;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.singletonIterator;
import static com.google.common.collect.Iterators.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.timgroup.karg.naming.StringFunctions.CAPITALISE;
import static com.timgroup.karg.naming.StringFunctions.LOWERCASE_TRIM;
import static com.timgroup.karg.naming.StringFunctions.NOT_EMPTY;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public abstract class TargetNameFormatter {
    
    public static final TargetNameFormatter LOWER_CAMEL_CASE = new LowerCamelCaseFormatter();
    public static final TargetNameFormatter UPPER_CAMEL_CASE = new UpperCamelCaseFormatter();
    public static final TargetNameFormatter UNDERSCORE_SEPARATED = new UnderscoreSeparatedFormatter();
    
    public static final class CamelCaseParser {
        
        public static TargetName parse(String nameString) {
            StringBuilder word = new StringBuilder();
            List<String> words = newLinkedList();
            for(char c: nameString.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    if (word.length() >0) {
                        words.add(word.toString());
                        word = new StringBuilder();
                    }
                }
                word.append(c);
            }
            if (word.length() > 0) {
                words.add(word.toString());
            }
            return new TargetName(words);
        }
    }
    
    private static final class LowerCamelCaseFormatter extends TargetNameFormatter {
        
        @Override
        public TargetName parse(String nameString) {
            return CamelCaseParser.parse(nameString);
        }
        
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
    
    private static final class UpperCamelCaseFormatter extends TargetNameFormatter {
        
        @Override
        public TargetName parse(String nameString) {
            return CamelCaseParser.parse(nameString);
        }
        
        @Override
        public String format(Iterable<String> words) {
            Iterator<String> upperCamelCased = transform(words.iterator(),
                                                         compose(CAPITALISE, LOWERCASE_TRIM));
            return Joiner.on("").join(Lists.newArrayList(upperCamelCased));
        }
    }
    
    private static final class UnderscoreSeparatedFormatter extends TargetNameFormatter {
        
        @Override
        public TargetName parse(String nameString) {
            Iterator<String> words = transform(filter(newArrayList(nameString.split("_")).iterator(),
                                                      NOT_EMPTY),
                                               LOWERCASE_TRIM);
            return new TargetName(Lists.newArrayList(words));
        }
        
        @Override
        public String format(Iterable<String> words) {
            Iterator<String> upperCamelCased = transform(words.iterator(), LOWERCASE_TRIM);
            return Joiner.on("_").join(newArrayList(upperCamelCased));
        }
    }
    
    public abstract TargetName parse(String nameString);
    public abstract String format(Iterable<String> words);
    
}
