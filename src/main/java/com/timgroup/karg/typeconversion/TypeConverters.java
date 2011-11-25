package com.timgroup.karg.typeconversion;

import com.timgroup.karg.reference.Gettable;
import com.timgroup.karg.reference.Getter;
import com.timgroup.karg.reference.Lens;
import com.timgroup.karg.reference.Ref;
import com.timgroup.karg.reference.Settable;
import com.timgroup.karg.reference.Setter;

public final class TypeConverters {
    
    public static <O, INNER, OUTER> Lens<O, OUTER> convert(final Lens<O, INNER> lens, final TypeConverter<INNER, OUTER> converter) {
        return new Lens<O, OUTER>() {
            @Override public OUTER get(O object) {
                return converter.pullOut(lens.get(object));
            }

            @Override public OUTER set(O object, OUTER newValue) {
                return converter.pullOut(lens.set(object, converter.pushIn(newValue)));
            }
        };
    }
    
    public static <O, INNER, OUTER> Getter<O, OUTER> convert(final Getter<O, INNER> lens, final TypeConverter<INNER, OUTER> converter) {
        return new Getter<O, OUTER>() {
            @Override public OUTER get(O object) {
                return converter.pullOut(lens.get(object));
            }
        };
    }
    
    public static <O, INNER, OUTER> Setter<O, OUTER> convert(final Setter<O, INNER> lens, final TypeConverter<INNER, OUTER> converter) {
        return new Setter<O, OUTER>() {
            @Override public OUTER set(O object, OUTER newValue) {
                return converter.pullOut(lens.set(object, converter.pushIn(newValue)));
            }
        };
    }
    
    public static <INNER, OUTER> Ref<OUTER> convert(final Ref<INNER> ref, final TypeConverter<INNER, OUTER> converter) {
        return new Ref<OUTER>() {
            @Override public OUTER get() {
                return converter.pullOut(ref.get());
            }

            @Override public OUTER set(OUTER newValue) {
                return converter.pullOut(ref.set(converter.pushIn(newValue)));
            }
        };
    }
    
    public static <INNER, OUTER> Gettable<OUTER> convert(final Gettable<INNER> ref, final TypeConverter<INNER, OUTER> converter) {
        return new Gettable<OUTER>() {
            @Override public OUTER get() {
                return converter.pullOut(ref.get());
            }
        };
    }
    
    public static <INNER, OUTER> Settable<OUTER> convert(final Settable<INNER> ref, final TypeConverter<INNER, OUTER> converter) {
        return new Settable<OUTER>() {
            @Override public OUTER set(OUTER newValue) {
                return converter.pullOut(ref.set(converter.pushIn(newValue)));
            }
            
        };
    }

    public static <INNER, OUTER> TypeConverter<OUTER, INNER> reverse(final TypeConverter<INNER, OUTER> converter) {
        return new TypeConverter<OUTER, INNER>() {
            @Override public OUTER pushIn(INNER outer) {
                return converter.pullOut(outer);
            }

            @Override public INNER pullOut(OUTER inner) {
                return converter.pushIn(inner);
            }
        };
    }
    
    public static final TypeConverter<Integer, String> INTEGER_TO_STRING = new TypeConverter<Integer, String>() {
        @Override public Integer pushIn(String outer) {
            return Integer.valueOf(outer);
        }

        @Override public String pullOut(Integer inner) {
            return inner.toString();
        }
    };
    
    public static <INNER, MIDDLE, OUTER> TypeConverter<INNER, OUTER> compose(final TypeConverter<INNER, MIDDLE> converter1,
                                                                             final TypeConverter<MIDDLE, OUTER> converter2) {
        return new TypeConverter<INNER, OUTER>() {

            @Override public INNER pushIn(OUTER outer) {
                return converter1.pushIn(converter2.pushIn(outer));
            }

            @Override public OUTER pullOut(INNER inner) {
                return converter2.pullOut(converter1.pullOut(inner));
            }
        };
    }
    
    private TypeConverters() { }
}
