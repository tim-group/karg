package com.timgroup.karg.typeconversion;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.timgroup.karg.reference.Cell;
import com.timgroup.karg.reference.Lens;
import com.timgroup.karg.reference.Lenses;
import com.timgroup.karg.reference.Ref;
import com.timgroup.karg.typeconversion.TypeConverter;
import com.timgroup.karg.typeconversion.TypeConverters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class TypeConvertersTest {
    
    @Test public void
    typeConvertersCanConvertLenses() {
        Lens<List<String>, Integer> firstNumber = TypeConverters.convert(Lenses.<String>firstItem(), TypeConverters.reverse(TypeConverters.INTEGER_TO_STRING));
        
        List<String> numbers = Lists.newArrayList("12", "13", "14");
        assertThat(firstNumber.get(numbers), is(12));
        
        firstNumber.set(numbers, 16);
        assertThat(numbers.get(0), is("16"));
    }
    
    
    @Test public void
    typeConvertersCanConvertRefs() {
        Ref<Integer> ref = Cell.of(12);
        Ref<String> convertedRef = TypeConverters.convert(ref, TypeConverters.INTEGER_TO_STRING);
        
        assertThat(convertedRef.get(), is("12"));
        convertedRef.set("13");
        assertThat(ref.get(), is(13));
        assertThat(convertedRef.get(), is("13"));
    }
    
    @Test public void
    typeConvertersCompose() {
        TypeConverter<Integer, Integer> stringToDouble = TypeConverters.compose(TypeConverters.INTEGER_TO_STRING,
                                                                                TypeConverters.reverse(TypeConverters.INTEGER_TO_STRING));
        
        assertThat(stringToDouble.pushIn(12), is(12));
        assertThat(stringToDouble.pullOut(12), is(12));
    }
    
    
}
