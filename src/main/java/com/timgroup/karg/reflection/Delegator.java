package com.timgroup.karg.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.common.base.Preconditions;

public class Delegator<T, I> {
    
    private final String methodName;
    private final Class<? super I> delegateClass;
    private final Class<? super T> targetClass;
    
    public static interface MethodNameBinder {
        <T> Delegator.TargetClassBinder<T> of(Class<? super T> targetClass);
    }
    
    public static interface TargetClassBinder<T> {
        <I> Delegator<T, I> to(Class<? super I> delegateClass);
    }
    
    public static Delegator.MethodNameBinder ofMethod(final String methodName) {
        return new MethodNameBinder() {
            @Override public <T> Delegator.TargetClassBinder<T> of(final Class<? super T> targetClass) {
                return new Delegator.TargetClassBinder<T>() {
                    @Override public <I> Delegator<T, I> to(Class<? super I> delegateClass) {
                       validate(methodName, targetClass, delegateClass);
                       return new Delegator<T, I>(methodName, targetClass, delegateClass);
                    }
                };
            }
        };
    }
    
    private static void validate(String methodName, Class<?> targetClass, Class<?> delegateClass) {
        try {
            Preconditions.checkArgument(delegateClass.isInterface(), "Delegate class must be an interface");
            Preconditions.checkArgument(delegateClass.getDeclaredMethods().length == 1,
                                        "Delegate interface must have only one argument");
            Method delegateMethod = delegateClass.getDeclaredMethods()[0];
            Method method = targetClass.getMethod(methodName, delegateMethod.getParameterTypes());
            Preconditions.checkArgument(delegateMethod.getReturnType().isAssignableFrom(method.getReturnType()));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Delegator(String methodName, Class<? super T> targetClass, Class<? super I> delegateClass) {
        this.methodName = methodName;
        this.targetClass = targetClass;
        this.delegateClass = delegateClass;
    }
    
    @SuppressWarnings("unchecked")
    I delegateTo(T instance) {
        return (I) Proxy.newProxyInstance(instance.getClass().getClassLoader(),
                                      new Class<?>[] { delegateClass },
                                      invocationHandlerFor(instance));
    }

    private InvocationHandler invocationHandlerFor(final T instance) {
        return new InvocationHandler() {
            @Override public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                Class<?>[] types = getArgumentTypes(args);
                return targetClass.getMethod(methodName, types).invoke(instance, args);
            }


            
        };
    }
    
    private Class<?>[] getArgumentTypes(Object[] args) {
        if (args == null) {
            return null;
        }
        Class<?>[] types = new Class<?>[args.length];
        for (int i=0; i<args.length; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }
}