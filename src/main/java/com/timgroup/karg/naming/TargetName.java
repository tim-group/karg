package com.timgroup.karg.naming;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class TargetName {

    public static TargetName fromMethodName(String methodName) {
        if (methodName.contains("_")) {
            return TargetNameParser.UNDERSCORE_SEPARATED.parse(methodName);
        }
        return TargetNameParser.CAMEL_CASE.parse(methodName);
    }
    
    private final Iterable<String> words;
    
    public TargetName(Iterable<String> words) {
        this.words = words;
    }
    
    public TargetName prefixedWith(String prefix) {
        return new TargetName(Iterables.concat(Lists.newArrayList(prefix), words));
    }
    
    public TargetName withSuffix(String suffix) {
        return new TargetName(Iterables.concat(words, Lists.newArrayList(suffix)));
    }
    
    public boolean hasPrefix(String prefix) {
        return prefix.equals(Iterables.getFirst(words, null));
    }
    
    public TargetName withoutPrefix() {
        return new TargetName(Iterables.skip(words, 1));
    }
    
    public String formatWith(TargetNameFormatter formatter) {
        return formatter.format(words);
    }
}
