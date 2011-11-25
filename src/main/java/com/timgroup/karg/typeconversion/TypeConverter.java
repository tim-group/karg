package com.timgroup.karg.typeconversion;

public interface TypeConverter<INNER, OUTER> {
    INNER pushIn(OUTER outer);
    OUTER pullOut(INNER inner);
}
