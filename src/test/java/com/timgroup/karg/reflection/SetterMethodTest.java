package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import org.junit.Test;


public class SetterMethodTest {
    public static class TestClass {
        public String value;
        public void setMeOnFire(String value) {
            this.value = value;
        }
    }
    
    private final TestClass testInstance = new TestClass();
    
    @Test(expected=Exception.class) public void
    static_constructor_throws_exception_if_requested_setter_does_not_exist() {
        SetterMethod.forProperty("notAppearingInThisTest").ofClass(TestClass.class);
    }
    
    @Test public void
    static_constructor_finds_setter_with_set_prefix() {
        SetterMethod<TestClass, String> method = SetterMethod.forProperty("meOnFire").ofClass(TestClass.class);
        assertThat(method.propertyName(), is("meOnFire"));
        method.set(testInstance, "kerosene");
        assertThat(testInstance.value, is("kerosene"));
    }
    
    @Test public void
    infers_property_name_from_method_name_with_set_prefix() throws NoSuchMethodException, SecurityException {
        Method rawMethod = TestClass.class.getMethod("setMeOnFire", String.class);
        SetterMethod<TestClass, String> method = SetterMethod.forMethod(rawMethod);
        assertThat(method.propertyName(), is("meOnFire"));
    }
}
