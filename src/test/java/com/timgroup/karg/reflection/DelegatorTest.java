package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.concurrent.Callable;

import org.junit.Test;

public class DelegatorTest {

    public static class TestClass {
        public String knockKnock() {
            return "who's there?";
        }
    }
    
    private final TestClass testInstance = new TestClass();
    
    @Test public void
    can_delegate_a_single_method_interface_to_an_instance() throws Exception {
        Delegator<TestClass, Callable<String>> knockKnockDelegator = Delegator.ofMethod("knockKnock")
                                                                     .of(TestClass.class)
                                                                     .to(Callable.class);
        Callable<String> callable = knockKnockDelegator.delegateTo(testInstance);
        assertThat(callable.call(), is("who's there?"));
    }

    public static interface TwoSheds {
        String shed1();
        String shed2();
    }
    
    @Test(expected=Exception.class) public void
    throws_exception_if_interface_has_more_than_one_method() {
        Delegator.ofMethod("knockKnock")
                 .of(TestClass.class)
                 .to(TwoSheds.class);
    }
    
    public static class Multiplier {
        public double multiply(double x, double y) {
            return x * y;
        }
    }
    
    private final Multiplier multiplier = new Multiplier();
    
    public static interface WrongReturnType {
        Integer op(Double lhs, Double rhs);
    }
    
    public static interface WrongParameters {
        Double op(Integer lhs, Double rhs);
    }
    
    public static interface RightParameters {
        double op(double lhs, double rhs);
    }
    
    @Test(expected=Exception.class) public void
    throws_exception_if_interface_method_return_type_does_not_match_target_method() {
        Delegator.ofMethod("multiply")
                 .of(Multiplier.class)
                 .to(WrongReturnType.class);
    }
    
    @Test(expected=Exception.class) public void
    throws_exception_if_interface_method_signature_does_not_match_target_method() {
        Delegator.ofMethod("multiply")
                 .of(Multiplier.class)
                 .to(WrongParameters.class);
    }
    
    @Test public void
    can_delegate_methods_with_multiple_parameters() {
        Delegator<Multiplier, RightParameters> multiplication = Delegator.ofMethod("multiply")
                                                                         .of(Multiplier.class)
                                                                         .to(RightParameters.class);
        RightParameters delegate = multiplication.delegateTo(multiplier);
        
        assertThat(delegate.op(4.0, 5.0), equalTo(4.0 * 5.0));
    }
    
    public static class MyException extends Exception {
        private static final long serialVersionUID = 1L; }
    
    public static interface Exceptional {
        void blowUp() throws Exception;
    }
    
    public static class Exploder {
        public void explode() throws MyException {
            throw new MyException();
        }
    }
    
    @Test(expected=MyException.class) public void
    passes_back_the_declared_exception_type_where_possible() throws Exception {
        Delegator<Exploder, Exceptional> delegator = Delegator.ofMethod("explode")
                                                              .of(Exploder.class)
                                                              .to(Exceptional.class);
        
        Exceptional exceptional = delegator.delegateTo(new Exploder());
        exceptional.blowUp();
    }
    
    @Test public void
    can_bind_directly_to_instances() {
        RightParameters delegate = Delegator.ofMethod("multiply").ofInstance(multiplier).to(RightParameters.class);
        assertThat(delegate.op(4.0, 5.0), equalTo(4.0 * 5.0));
    }
}
