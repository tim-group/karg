package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.timgroup.karg.reference.Getter;
import com.timgroup.karg.reference.Setter;

public class ReflectiveAccessorFactoryTest {

    public static class TestClass {
        public final String bar = "bar-value";
        private String encapsulated = "";
        public String getBaz() {
            return encapsulated;
        }
        public void setBaz(String newValue) {
            encapsulated = newValue;
        }
    }
    
    private final TestClass testInstance = new TestClass();
    
    @Test(expected=Exception.class) public void
    throws_an_exception_if_asked_for_a_getter_that_does_not_exist() {
        ReflectiveAccessorFactory<TestClass> fac = ReflectiveAccessorFactory.forClass(TestClass.class);
        fac.getGetter("foo");
    }
    
    @Test public void
    returns_a_getter_for_a_readable_field() {
        ReflectiveAccessorFactory<TestClass> fac = ReflectiveAccessorFactory.forClass(TestClass.class);
        Getter<TestClass, String> getter = fac.getGetter("bar");
        assertThat(getter.get(testInstance), is("bar-value"));
    }
    
    @Test(expected=Exception.class) public void
    throws_an_exception_if_asked_for_a_setter_that_does_not_exist() {
        ReflectiveAccessorFactory<TestClass> fac = ReflectiveAccessorFactory.forClass(TestClass.class);
        fac.getSetter("bar");
    }
    
    @Test public void
    returns_a_setter_for_a_writable_field() {
        ReflectiveAccessorFactory<TestClass> fac = ReflectiveAccessorFactory.forClass(TestClass.class);
        Setter<TestClass, String> setter = fac.getSetter("baz");
        setter.set(testInstance, "new value");
        assertThat(testInstance.getBaz(), is("new value"));
    }
}
