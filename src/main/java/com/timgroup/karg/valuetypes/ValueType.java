package com.timgroup.karg.valuetypes;

import static com.google.common.collect.Sets.newTreeSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.timgroup.karg.keywords.typed.TypedKeyword;
import com.timgroup.karg.keywords.typed.TypedKeywordArgument;
import com.timgroup.karg.keywords.typed.TypedKeywordArguments;

public abstract class ValueType<T extends ValueType<T>> {

    private static final class KeywordNameLookupMaker extends CacheLoader<Class<?>, Map<TypedKeyword<?, ?>, String>> {
        @Override
        public Map<TypedKeyword<?, ?>, String> load(Class<?> key) throws Exception {
             Map<TypedKeyword<?, ?>, String> keywordNameLookup = Maps.newHashMap();
             for (Field field : key.getDeclaredFields()) {
                 if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(TypedKeyword.class)) {
                     keywordNameLookup.put((TypedKeyword<?, ?>) field.get(null), field.getName());
                     
                 }
             }
             return keywordNameLookup;
         }
    }

    private static final Cache<Class<?>, Map<TypedKeyword<?, ?>, String>> keywordNameLookups =
            CacheBuilder.newBuilder().build(
           new KeywordNameLookupMaker());
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Map<TypedKeyword<T, ?>, String> keywordNameLookup(Class<?> valueTypeType) {
        try {
            return (Map) keywordNameLookups.get(valueTypeType);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    private final TypedKeywordArguments<T> fields;
    private final Map<TypedKeyword<T, ?>, String> keywordNameLookup = keywordNameLookup(getClass());
    
    public ValueType(TypedKeywordArguments<T> fields) {
        this.fields = fields;
    }

    public TypedKeywordArguments<T> fields() {
        return fields;
    }
    
    public <S> S fieldValue(TypedKeyword<T, S> keyword) {
        return keyword.from(fields);
    }

    public T copy(TypedKeywordArgument<T>... keywordArguments) {
        return copy(TypedKeywordArguments.of(keywordArguments));
    }

    public T copy(TypedKeywordArguments<T> keywordArguments) {
        return makeNew(fields.with(keywordArguments));
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof ValueType)) {
            return false;
        }

        ValueType<T> other = (ValueType<T>) obj;
        return fields.equals(other.fields);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(getClass().getSimpleName());
        
        sb.append(" [fields=");
        
        boolean first = true;
        for (TypedKeyword<T, ?> keyword : orderedKeywords()) {
            if (first) { first = false; } else { sb.append(", "); }
            sb.append(keywordName(keyword))
              .append(": ")
              .append(keyword.from(fields));
        }
        
        sb.append("]");
        return sb.toString();
    }

    public Set<TypedKeyword<T, ?>> keywords() {
        return ImmutableSet.copyOf(fields.keySet());
    }
    
    public String keywordName(TypedKeyword<T, ?> keyword) {
        return keywordNameLookup.get(keyword);
    }
    
    public List<TypedKeyword<T, ?>> orderedKeywords() {
        Set<TypedKeyword<T, ?>> orderedKeywords = newTreeSet(nameLookupComparator(keywordNameLookup));
        orderedKeywords.addAll(fields.keySet());
        return ImmutableList.copyOf(orderedKeywords);
    }
    
    private static <T> Comparator<? super TypedKeyword<T, ?>> nameLookupComparator(final Map<TypedKeyword<T, ?>, String> keywordNameLookup) {
        return new Comparator<TypedKeyword<T, ?>>() {
            @Override
            public int compare(TypedKeyword<T, ?> o1, TypedKeyword<T, ?> o2) {
                String name1 = keywordNameLookup.get(o1);
                String name2 = keywordNameLookup.get(o2);
                return name1.compareTo(name2);
            }
        };
    }

    @SuppressWarnings("unchecked")
    protected T makeNew(TypedKeywordArguments<T> keywordArguments) {
        try {
            return (T) this.getClass().getConstructor(TypedKeywordArguments.class).newInstance(keywordArguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Cannot reflectively instantiate a new instance of %s - override makeNew manually", getClass()));
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("Cannot reflectively instantiate a new instance of %s - override makeNew manually", getClass()));    
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Class %s does not have TypedKeywordArguments constructor", getClass()));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }
}

