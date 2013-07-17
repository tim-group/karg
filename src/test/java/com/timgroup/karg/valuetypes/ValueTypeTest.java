package com.timgroup.karg.valuetypes;

import static com.timgroup.karg.keywords.typed.TypedKeywords.newTypedKeyword;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.timgroup.karg.keywords.typed.TypedKeyword;
import com.timgroup.karg.keywords.typed.TypedKeywordArgument;
import com.timgroup.karg.keywords.typed.TypedKeywordArguments;

public class ValueTypeTest {

    private static class TestValueType extends ValueType<TestValueType> {

        public static final TypedKeyword<TestValueType, String> NAME = newTypedKeyword();
        public final String name;
        
        public static final TypedKeyword<TestValueType, Integer> AGE = newTypedKeyword();
        public final int age;
        
        public TestValueType(TypedKeywordArguments<TestValueType> fields) {
            super(fields);
            name = NAME.from(fields);
            age = AGE.from(fields);
        }
        
        public TestValueType(TypedKeywordArgument<TestValueType>...fields) {
            this(TypedKeywordArguments.of(fields));
        }
        
    }
    @SuppressWarnings("unchecked")
    @Test public void
    a_value_type_exposes_its_fields() {
        TestValueType testValue = new TestValueType(
            TestValueType.NAME.of("Hello"),
            TestValueType.AGE.of(42));
        
        assertThat(testValue.fields(),
                equalTo(TypedKeywordArguments.of(TestValueType.NAME.of("Hello"), TestValueType.AGE.of(42))));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    a_value_type_exposes_individual_fields() {
        TestValueType testValue = new TestValueType(
            TestValueType.NAME.of("Hello"),
            TestValueType.AGE.of(42));
        
        assertThat(testValue.fieldValue(TestValueType.NAME), equalTo(testValue.name));
        assertThat(testValue.fieldValue(TestValueType.AGE), equalTo(testValue.age));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    a_value_type_can_be_copied() {
        TestValueType testValue = new TestValueType(
            TestValueType.NAME.of("Hello"),
            TestValueType.AGE.of(42));
        
        assertThat(testValue.copy(TestValueType.AGE.of(23)), equalTo(new TestValueType(
            TestValueType.NAME.of("Hello"),
            TestValueType.AGE.of(23))));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    a_value_type_has_a_meaningful_toString() {
        TestValueType testValue = new TestValueType(
            TestValueType.NAME.of("Hello"),
            TestValueType.AGE.of(42));
        
        assertThat(testValue.toString(), equalTo("TestValueType [fields=AGE: 42, NAME: Hello]"));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    a_value_type_knows_the_names_of_its_keywords() {
        TestValueType testValue = new TestValueType(
            TestValueType.NAME.of("Hello"),
            TestValueType.AGE.of(42));
        
        assertThat(testValue.keywordName(TestValueType.AGE), equalTo("AGE"));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    a_value_type_lists_its_keywords_ordered_by_name() {
        TestValueType testValue = new TestValueType(
            TestValueType.NAME.of("Hello"),
            TestValueType.AGE.of(42));
        
        assertThat(testValue.orderedKeywords(), Matchers.<TypedKeyword<TestValueType, ?>>contains(
            TestValueType.AGE, TestValueType.NAME));
    }
}
