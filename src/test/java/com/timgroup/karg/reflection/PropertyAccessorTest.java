package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class PropertyAccessorTest {

    public static class TestClass {
        private String testString1 = "test1";
        private String testString2 = "test2";
        private String testString3 = "test3";

        @SuppressWarnings("unused")
        private String getTestString1() {
            return testString1;
        }
        
        public String testString3() {
            return testString3;
        }
        public void set_test_string3(String testString3) {
            this.testString3 = testString3;
        }
        public String getTestString2() {
            return testString2;
        }
    }
    
    private final TestClass testInstance = new TestClass();
    
    @Test(expected=RuntimeException.class) public void
    static_constructor_throws_exception_if_property_does_not_exist() {
        PropertyAccessor.forProperty("inexistent").ofClass(TestClass.class);
    }
    
    @Test(expected=RuntimeException.class) public void
    static_constructor_throws_exception_if_property_is_inaccessible() {
        PropertyAccessor.forProperty("testString1").ofClass(TestClass.class);
    }
    
    @Test public void
    can_get_the_value_of_a_public_property() {
        Accessor<TestClass, String> accessor = PropertyAccessor.forProperty("testString2").ofClass(TestClass.class);
        assertThat(accessor.get(testInstance), is(testInstance.testString2));
    }
    
    @Test public void
    properties_without_setters_are_not_mutable() {
        Accessor<TestClass, String> accessor = PropertyAccessor.forProperty("testString2").ofClass(TestClass.class);
        assertThat(accessor.isMutable(), is(false));
    }
    
    @Test public void
    properties_with_setters_are_mutable() {
        Accessor<TestClass, String> accessor = PropertyAccessor.forProperty("testString3").ofClass(TestClass.class);
        assertThat(accessor.isMutable(), is(true));
    }
    
    @Test public void
    can_modify_the_value_of_a_mutable_property() {
        Accessor<TestClass, String> accessor = PropertyAccessor.forProperty("testString3").ofClass(TestClass.class);
        accessor.set(testInstance, "mutated");
        assertThat(testInstance.testString3, is("mutated"));
    }
}
