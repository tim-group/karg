package com.timgroup.karg.keywords;

public interface TypeConverter<INNER, OUTER> {
    INNER pushIn(OUTER outer);
    OUTER pullOut(INNER inner);
}
