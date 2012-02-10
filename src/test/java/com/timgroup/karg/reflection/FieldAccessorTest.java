package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class FieldAccessorTest {

    @SuppressWarnings("unused")
    private final String testString = "test1";
    public final String testString2 = "test2";
    public String testString3 = "test3";
    
    @Test(expected=Exception.class) public void
    static_constructor_throws_exception_if_field_does_not_exist() {
        FieldAccessor.forField("inexistent").ofClass(FieldAccessorTest.class);
    }
    
    @Test(expected=Exception.class) public void
    static_constructor_throws_exception_if_field_is_inaccessible() {
        FieldAccessor.forField("testString1").ofClass(FieldAccessorTest.class);
    }
    
    @Test public void
    has_the_name_of_the_field_it_accesses() {
        Accessor<FieldAccessorTest, String> accessor = FieldAccessor.forField("testString2").ofClass(FieldAccessorTest.class);
        assertThat(accessor.propertyName(), is("testString2"));
    }
    
    @Test public void
    can_get_the_value_of_a_public_field() {
        Accessor<FieldAccessorTest, String> accessor = FieldAccessor.forField("testString2").ofClass(FieldAccessorTest.class);
        assertThat(accessor.get(this), is(testString2));
    }
    
    @Test public void
    final_fields_are_not_mutable() {
        Accessor<FieldAccessorTest, String> accessor = FieldAccessor.forField("testString2").ofClass(FieldAccessorTest.class);
        assertThat(accessor.isMutable(), is(false));
    }
    
    @Test public void
    non_final_fields_are_mutable() {
        Accessor<FieldAccessorTest, String> accessor = FieldAccessor.forField("testString3").ofClass(FieldAccessorTest.class);
        assertThat(accessor.isMutable(), is(true));
    }
    
    @Test public void
    can_modify_the_value_of_a_mutable_public_field() {
        Accessor<FieldAccessorTest, String> accessor = FieldAccessor.forField("testString3").ofClass(FieldAccessorTest.class);
        accessor.set(this, "mutated");
        assertThat(testString3, is("mutated"));
    }
}
