package com.timgroup.karg.naming;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public final class StringFunctions {

    private StringFunctions() { }

    public static final Function<String, String> TRIM = new Function<String, String>() {
        @Override
        public String apply(String arg0) {
            return arg0.trim();
        }
    };
    
    public static final Function<String, String> LOWERCASE = new Function<String, String>() {
        @Override
        public String apply(String arg0) {
            return arg0.toLowerCase();
        }
    };
    
    public static final Function<String, String> LOWERCASE_TRIM = Functions.compose(LOWERCASE, TRIM);
    
    public static final Function<String, String> CAPITALISE = new Function<String, String>() {
        @Override
        public String apply(String arg0) {
            switch(arg0.length()) {
                case 0: return "";
                case 1: return arg0.toUpperCase();
                default: return arg0.substring(0, 1).toUpperCase() + arg0.substring(1);
            }
        }
    };
    
    public static final Predicate<String> EMPTY = new Predicate<String>() {
        @Override public boolean apply(String arg0) {
            return arg0.isEmpty();
        }
    };
    
    public static final Predicate<String> NOT_EMPTY = Predicates.not(EMPTY);
}
