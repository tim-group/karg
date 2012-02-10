package com.timgroup.karg.keywords.typed;


public class TypedKeywordArgument<O> {

    private final TypedKeyword<O, ?> keyword;
    private final Object value;

    public static <O, T> TypedKeywordArgument<O> value(TypedKeyword<O, T> keyword, T value) {
        return new TypedKeywordArgument<O>(keyword, value);
    }
    
    private <T> TypedKeywordArgument(TypedKeyword<O, T> keyword, T value) {
        this.keyword = keyword;
        this.value = value;
    }
    
    public TypedKeyword<O, ?> keyword() {
        return keyword;
    }
    
    public Object value() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TypedKeywordArgument other = (TypedKeywordArgument) obj;
        if (!keyword.equals(other.keyword)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
    
}
