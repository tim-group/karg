package com.timgroup.karg.keywords.typed;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArguments;

public class TypedKeywordArguments<O> implements Map<TypedKeyword<O, ?>, TypedKeywordArgument<O>> {

    private final Map<TypedKeyword<O, ?>, TypedKeywordArgument<O>> keywordArguments = newHashMap();
    
    @SuppressWarnings("unchecked")
    public static <O> TypedKeywordArguments<O> of(TypedKeywordArgument<O> keywordArgument) {
        return of(newArrayList(keywordArgument));
    }
    
    public static <O> TypedKeywordArguments<O> of(TypedKeywordArgument<O>...keywordArgumentArgs) {
        return of(newArrayList(keywordArgumentArgs));
    }
    
    @SuppressWarnings("unchecked")
    public static <O> TypedKeywordArguments<O> empty() {
        return of();
    }
    
    public static <O> TypedKeywordArguments<O> of(List<TypedKeywordArgument<O>> keywordArgumentList) {
        return new TypedKeywordArguments<O>(keywordArgumentList);
    }
    
    private TypedKeywordArguments(List<TypedKeywordArgument<O>> keywordArgumentList) {
        for (TypedKeywordArgument<O> keywordArgument : keywordArgumentList) {
            keywordArguments.put(keywordArgument.keyword(), keywordArgument);
        }
    }
    
    public boolean matchesKeywords(Set<TypedKeyword<O, ?>> keywords) {
        return keywordArguments.keySet().equals(keywords);
    }

    public <T> T valueOf(TypedKeyword<O, T> keyword) {
        return valueOf(keyword, null);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T valueOf(TypedKeyword<O, T> keyword, T defaultValue) {
        TypedKeywordArgument<O> argument = keywordArguments.get(keyword);
        return argument == null ? defaultValue : (T) argument.value();
    }

    @SuppressWarnings("unchecked")
    public TypedKeywordArguments<O> with(TypedKeywordArgument<O> additionalArgument) {
        return with(newArrayList(additionalArgument));
    }
    
    public TypedKeywordArguments<O> with(TypedKeywordArgument<O>...additionalArguments) {
        return with(newArrayList(additionalArguments));
    }

    public TypedKeywordArguments<O> with(List<TypedKeywordArgument<O>> additionalArguments) {
        List<TypedKeywordArgument<O>> argumentList = newLinkedList(keywordArguments.values());
        argumentList.addAll(additionalArguments);
        return new TypedKeywordArguments<O>(argumentList);
    }
    
    public TypedKeywordArguments<O> with(TypedKeywordArguments<O> additionalArguments) {
        List<TypedKeywordArgument<O>> argumentList = newLinkedList(keywordArguments.values());
        argumentList.addAll(additionalArguments.keywordArguments.values());
        return new TypedKeywordArguments<O>(argumentList);
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

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || !(obj instanceof TypedKeywordArguments)) {
            return false;
        }
        
        TypedKeywordArguments other = (TypedKeywordArguments) obj;
        return keywordArguments.equals(other.keywordArguments);
    }
    @Override
    public void clear() {
        keywordArguments.clear();
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
    public Set<java.util.Map.Entry<TypedKeyword<O, ?>, TypedKeywordArgument<O>>> entrySet() {
        return Sets.newHashSet(keywordArguments.entrySet());
    }

    @Override
    public TypedKeywordArgument<O> get(Object key) {
        return keywordArguments.get(key);
    }

    @Override
    public boolean isEmpty() {
        return keywordArguments.isEmpty();
    }

    @Override
    public Set<TypedKeyword<O, ?>> keySet() {
        return Sets.newHashSet(keywordArguments.keySet());
    }

    @Override
    public TypedKeywordArgument<O> put(TypedKeyword<O, ?> key, TypedKeywordArgument<O> value) {
        return keywordArguments.put(key, value);
    }

    @Override
    public void putAll(Map<? extends TypedKeyword<O, ?>, ? extends TypedKeywordArgument<O>> m) {
        keywordArguments.putAll(m);
    }

    @Override
    public TypedKeywordArgument<O> remove(Object key) {
        return keywordArguments.remove(key);
    }

    @Override
    public int size() {
        return keywordArguments.size();
    }

    @Override
    public Collection<TypedKeywordArgument<O>> values() {
        return Lists.newArrayList(keywordArguments.values());
    }
    
}
