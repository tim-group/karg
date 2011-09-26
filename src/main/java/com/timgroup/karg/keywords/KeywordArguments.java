package com.timgroup.karg.keywords;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;

public class KeywordArguments implements Map<Keyword<?>, KeywordArgument> {

    private final Map<Keyword<?>, KeywordArgument> keywordArguments = newHashMap();

    public static KeywordArguments of(KeywordArgument...keywordArgumentArgs) {
        return of(newArrayList(keywordArgumentArgs));
    }
    
    public static KeywordArguments of(List<KeywordArgument> keywordArgumentList) {
        return new KeywordArguments(keywordArgumentList);
    }
    
    private KeywordArguments(List<KeywordArgument> keywordArgumentList) {
        for (KeywordArgument keywordArgument : keywordArgumentList) {
            keywordArguments.put(keywordArgument.keyword(), keywordArgument);
        }
    }
    
    public boolean matchesKeywords(Set<Keyword<?>> keywords) {
        return keywordArguments.keySet().equals(keywords);
    }

    public <T> T valueOf(Keyword<T> keyword) {
        return valueOf(keyword, null);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T valueOf(Keyword<T> keyword, T defaultValue) {
        KeywordArgument argument = keywordArguments.get(keyword);
        return argument == null ? defaultValue : (T) argument.value();
    }

    public KeywordArguments with(KeywordArgument...additionalArguments) {
        return with(newArrayList(additionalArguments));
    }

    public KeywordArguments with(List<KeywordArgument> additionalArguments) {
        List<KeywordArgument> argumentList = newLinkedList(keywordArguments.values());
        argumentList.addAll(additionalArguments);
        return new KeywordArguments(argumentList);
    }
    
    public KeywordArguments with(KeywordArguments additionalArguments) {
        List<KeywordArgument> argumentList = newLinkedList(keywordArguments.values());
        argumentList.addAll(additionalArguments.keywordArguments.values());
        return new KeywordArguments(argumentList);
    }

    public static Predicate<KeywordArguments> containing(Keyword<?>...keywords) {
        final Set<Keyword<?>> keywordSet = Sets.newHashSet(keywords);
        return new Predicate<KeywordArguments>() {
            @Override
            public boolean apply(KeywordArguments input) {
                return input.matchesKeywords(keywordSet);
            }
        };
    }

    @Override
    public int hashCode() {
        return keywordArguments.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || !(obj instanceof KeywordArguments)) {
            return false;
        }
        
        KeywordArguments other = (KeywordArguments) obj;
        return keywordArguments.equals(other.keywordArguments);
    }

    @Override
    public int size() {
        return keywordArguments.size();
    }

    @Override
    public boolean isEmpty() {
        return keywordArguments.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keywordArguments.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return keywordArguments.containsValue(value);
    }

    @Override
    public KeywordArgument get(Object key) {
        return keywordArguments.get(key);
    }

    @Override
    public void putAll(Map<? extends Keyword<?>, ? extends KeywordArgument> m) {
        keywordArguments.putAll(m);
    }

    @Override
    public void clear() {
        keywordArguments.clear();
    }

    @Override
    public Set<Keyword<?>> keySet() {
        return Sets.newHashSet(keywordArguments.keySet());
    }

    @Override
    public Collection<KeywordArgument> values() {
        return Lists.newArrayList(keywordArguments.values());
    }

    @Override
    public Set<java.util.Map.Entry<Keyword<?>, KeywordArgument>> entrySet() {
        return Sets.newHashSet(keywordArguments.entrySet());
    }

    @Override
    public KeywordArgument put(Keyword<?> key, KeywordArgument value) {
        return keywordArguments.put(key, value);
    }

    @Override
    public KeywordArgument remove(Object key) {
        return keywordArguments.remove(key);
    }
}
