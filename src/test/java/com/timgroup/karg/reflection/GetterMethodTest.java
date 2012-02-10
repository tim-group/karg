package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import org.junit.Test;


public class GetterMethodTest {

    public static class TestClass {
        public boolean isATestMethod() {
            return true;
        }
        
        public int getMeaningOfLife() {
            return 42;
        }
        
        public String name() {
            return "Slim Shady";
        }
    }
    
    private final TestClass testInstance = new TestClass();
    
    @Test(expected=Exception.class) public void
    static_constructor_throws_exception_if_requested_getter_does_not_exist() {
        GetterMethod.forProperty("notAppearingInThisTest").ofClass(TestClass.class);
    }
    
    @Test public void
    static_constructor_finds_getters_with_is_prefix() {
        GetterMethod<TestClass, Boolean> method = GetterMethod.forProperty("aTestMethod").ofClass(TestClass.class);
        assertThat(method.propertyName(), is("aTestMethod"));
        assertThat(method.get(testInstance), is(true));
    }
    
    @Test public void
    static_constructor_finds_getters_with_get_prefix() {
        GetterMethod<TestClass, Integer> method = GetterMethod.forProperty("meaningOfLife").ofClass(TestClass.class);
        assertThat(method.propertyName(), is("meaningOfLife"));
        assertThat(method.get(testInstance), is(42));
    }
    
    @Test public void
    static_constructor_finds_getters_with_no_prefix() {
        GetterMethod<TestClass, String> method = GetterMethod.forProperty("name").ofClass(TestClass.class);
        assertThat(method.propertyName(), is("name"));
        assertThat(method.get(testInstance), is(is(is("Slim Shady"))));
    }
    
    @Test public void
    infers_property_name_from_method_name_with_is_prefix() throws NoSuchMethodException, SecurityException {
        Method rawMethod = TestClass.class.getMethod("isATestMethod");
        GetterMethod<TestClass, Boolean> method = GetterMethod.forMethod(rawMethod);
        assertThat(method.propertyName(), is("aTestMethod"));
    }
    
    @Test public void
    infers_property_name_from_method_name_with_get_prefix() throws NoSuchMethodException, SecurityException {
        Method rawMethod = TestClass.class.getMethod("getMeaningOfLife");
        GetterMethod<TestClass, Integer> method = GetterMethod.forMethod(rawMethod);
        assertThat(method.propertyName(), is("meaningOfLife"));
    }
    
    @Test public void
    infers_property_name_from_method_name_with_no_prefix() throws NoSuchMethodException, SecurityException {
        Method rawMethod = TestClass.class.getMethod("name");
        GetterMethod<TestClass, String> method = GetterMethod.forMethod(rawMethod);
        assertThat(method.propertyName(), is("name"));
    }
}
