package com.timgroup.karg.naming;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class TargetName {

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
    
    public String formatWith(TargetNameFormatter formatter) {
        return formatter.format(words);
    }
}
