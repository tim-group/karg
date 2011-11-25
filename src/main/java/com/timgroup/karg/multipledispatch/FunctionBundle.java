package com.timgroup.karg.multipledispatch;


public interface FunctionBundle<T> {

    void register(CandidateFunctionRegistry<T> registry);
    
}
