package com.timgroup.karg.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.common.base.Preconditions;

public class Delegator<T, I> {
    
    private final Method targetMethod;
    private final Class<? super I> delegateClass;
    
    public static final class MethodNameBinder {
        private final String methodName;
        private MethodNameBinder(String methodName) {
            this.methodName = methodName;
        }
        
        public <T> Delegator.TargetClassBinder<T> of(final Class<? super T> targetClass) {
            return new Delegator.TargetClassBinder<T>() {
                @Override public <I> Delegator<T, I> to(Class<? super I> delegateClass) {
                   Method targetMethod = getTargetMethod(methodName, targetClass, delegateClass);
                   return new Delegator<T, I>(targetMethod, delegateClass);
                }
            };
        }
        
        public <T> ProxyBinder<T> ofInstance(final T targetInstance) {
            return new ProxyBinder<T>() {
                @SuppressWarnings("unchecked")
                @Override public <I> I to(Class<? super I> delegateClass) {
                    Class<T> targetClass = (Class<T>) targetInstance.getClass();
                    return ofMethod(methodName).of(targetClass).<I>to(delegateClass).delegateTo(targetInstance);
                }
            };
        }
    }
    
    public static interface TargetClassBinder<T> {
        <I> Delegator<T, I> to(Class<? super I> delegateClass);
    }
    
    public static interface ProxyBinder<T> {
        <I> I to(Class<? super I> delegateClass);
    }
    
    public static Delegator.MethodNameBinder ofMethod(final String methodName) {
        return new MethodNameBinder(methodName);
    }
    
    private static Method getTargetMethod(String methodName, Class<?> targetClass, Class<?> delegateClass) {
        try {
            Preconditions.checkArgument(delegateClass.isInterface(), "Delegate class must be an interface");
            Preconditions.checkArgument(delegateClass.getDeclaredMethods().length == 1,
                                        "Delegate interface must have only one argument");
            Method delegateMethod = delegateClass.getDeclaredMethods()[0];
            Method method = targetClass.getMethod(methodName, delegateMethod.getParameterTypes());
            Preconditions.checkArgument(delegateMethod.getReturnType().isAssignableFrom(method.getReturnType()));
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Delegator(Method targetMethod, Class<? super I> delegateClass) {
        this.targetMethod = targetMethod;
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
                try {
                    return targetMethod.invoke(instance, args);
                } catch (InvocationTargetException e) {
                    Class<? extends Throwable> targetExceptionClass = e.getTargetException().getClass();
                    for(Class<?> exceptionType : method.getExceptionTypes()) {
                        if (exceptionType.isAssignableFrom(targetExceptionClass)) {
                            throw e.getTargetException();
                        }
                    }
                    throw e;
                }
            }
        };
    }
}