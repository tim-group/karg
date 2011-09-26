package com.timgroup.karg.keywords;

public class KeywordArgument {
    
    private final Keyword<?> keyword;
    private final Object value;

    public static <T> KeywordArgument value(Keyword<? super T> keyword, T value) {
        return new KeywordArgument(keyword, value);
    }
    
    private <T> KeywordArgument(Keyword<? super T> name, T value) {
        this.keyword = name;
        this.value = value;
    }
    
    public Keyword<?> keyword() {
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
        KeywordArgument other = (KeywordArgument) obj;
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
