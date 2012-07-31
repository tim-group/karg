package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

public class PropertyAccessorFinderTest {

    @SuppressWarnings("unchecked")
    @Test public void
    finds_bean_getters() {
        class TestClass {
            @SuppressWarnings("unused")
            public String getFoo() { return "value"; }
            
            @SuppressWarnings("unused")
            public boolean isBar() { return true; }
        }
        TestClass instance = new TestClass();
        Map<String, Accessor<TestClass, ?>> propertyAccessors = PropertyAccessorFinder.forClass(TestClass.class).find();
        
        Accessor<TestClass, String> fooAccessor = (Accessor<TestClass, String>) propertyAccessors.get("foo");
        assertThat(fooAccessor.propertyName(), is("foo"));
        assertThat(fooAccessor.isMutable(), is(false));
        assertThat(fooAccessor.get(instance), is("value"));
        
        Accessor<TestClass, Boolean> barAccessor = (Accessor<TestClass, Boolean>) propertyAccessors.get("bar");
        assertThat(barAccessor.propertyName(), is("bar"));
        assertThat(barAccessor.isMutable(), is(false));
        assertThat(barAccessor.get(instance), is(true));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    applies_bean_naming_conventions_to_underscore_named_getters() {
        class TestClass {
            @SuppressWarnings("unused")
            public String get_foo() { return "value"; }
            
            @SuppressWarnings("unused")
            public boolean is_bar() { return true; }
        }
        TestClass instance = new TestClass();
        Map<String, Accessor<TestClass, ?>> propertyAccessors = PropertyAccessorFinder.forClass(TestClass.class).find();
        
        Accessor<TestClass, String> fooAccessor = (Accessor<TestClass, String>) propertyAccessors.get("foo");
        assertThat(fooAccessor.propertyName(), is("foo"));
        assertThat(fooAccessor.isMutable(), is(false));
        assertThat(fooAccessor.get(instance), is("value"));
        
        Accessor<TestClass, Boolean> barAccessor = (Accessor<TestClass, Boolean>) propertyAccessors.get("bar");
        assertThat(barAccessor.propertyName(), is("bar"));
        assertThat(barAccessor.isMutable(), is(false));
        assertThat(barAccessor.get(instance), is(true));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    detects_non_bean_compliant_methods_with_getter_signature() {
        class TestClass {
            @SuppressWarnings("unused")
            public String foo() { return "value"; }
            
            @SuppressWarnings("unused")
            public boolean bar() { return true; }
        }
        TestClass instance = new TestClass();
        Map<String, Accessor<TestClass, ?>> propertyAccessors = PropertyAccessorFinder.forClass(TestClass.class).find();
        
        Accessor<TestClass, String> fooAccessor = (Accessor<TestClass, String>) propertyAccessors.get("foo");
        assertThat(fooAccessor.propertyName(), is("foo"));
        assertThat(fooAccessor.isMutable(), is(false));
        assertThat(fooAccessor.get(instance), is("value"));
        
        Accessor<TestClass, Boolean> barAccessor = (Accessor<TestClass, Boolean>) propertyAccessors.get("bar");
        assertThat(barAccessor.propertyName(), is("bar"));
        assertThat(barAccessor.isMutable(), is(false));
        assertThat(barAccessor.get(instance), is(true));
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class) public void
    throws_exception_if_clash_between_bean_compliant_and_non_bean_compliant_names() {
        class TestClass {
            @SuppressWarnings("unused")
            public String foo() { return "value"; }
            
            @SuppressWarnings("unused")
            public String get_foo() { return "schmalue"; }
        }
        TestClass instance = new TestClass();
        Map<String, Accessor<TestClass, ?>> propertyAccessors = PropertyAccessorFinder.forClass(TestClass.class).find();
        
        Accessor<TestClass, String> fooAccessor = (Accessor<TestClass, String>) propertyAccessors.get("foo");
        assertThat(fooAccessor.propertyName(), is("foo"));
        assertThat(fooAccessor.isMutable(), is(false));
        assertThat(fooAccessor.get(instance), is("value"));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    detects_getter_setter_pairs() {
        class TestClass {
            private String foo = "value";
            @SuppressWarnings("unused")
            public String foo() { return foo; }
            
            @SuppressWarnings("unused")
            public void setFoo(String newValue) { foo = newValue; }
        }
        TestClass instance = new TestClass();
        Map<String, Accessor<TestClass, ?>> propertyAccessors = PropertyAccessorFinder.forClass(TestClass.class).find();
        
        Accessor<TestClass, String> fooAccessor = (Accessor<TestClass, String>) propertyAccessors.get("foo");
        assertThat(fooAccessor.propertyName(), is("foo"));
        assertThat(fooAccessor.isMutable(), is(true));
        assertThat(fooAccessor.get(instance), is("value"));
        
        fooAccessor.set(instance, "new value");
        assertThat(fooAccessor.get(instance), is("new value"));
    }
    
    @Test(expected=IllegalStateException.class) public void
    throws_exception_if_write_only_setter_detected() {
        class TestClass {
            @SuppressWarnings("unused")
            public String foo = "value";
            
            @SuppressWarnings("unused")
            public void setFoo(String newValue) { foo = newValue; }
        }
        Map<String, Accessor<TestClass, ?>> propertyAccessors = PropertyAccessorFinder.forClass(TestClass.class).find();

        assertThat(propertyAccessors.get("foo"), Matchers.nullValue());
    }
    
    @Test(expected=IllegalStateException.class) public void
    throws_exception_if_getter_setter_pair_have_incompatible_types() {
        class TestClass {
            private String foo = "value";
            @SuppressWarnings("unused")
            public String foo() { return foo; }
            
            @SuppressWarnings("unused")
            public void setFoo(Boolean newValue) { foo = newValue.toString(); }
        }
        PropertyAccessorFinder.forClass(TestClass.class).find();
    }
    
}