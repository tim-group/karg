package com.timgroup.karg.reference;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.timgroup.karg.reference.Cell;
import com.timgroup.karg.reference.Ref;
import com.timgroup.karg.reference.Refs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;


public class RefsTest {

    @Test public void
    anyRefCanBeAnIterable() {
        Iterable<String> iterable = Refs.toIterable(Cell.of("Hello"));
        assertThat(iterable.iterator().hasNext(), is(true));
        assertThat(iterable.iterator().next(), is("Hello"));
    }
    
    @Test public void
    anEmptyRefIsAnEmptyIterable() {
        Ref<String> ref = Cell.of(null);
        Iterable<String> iterable = Refs.toIterable(ref);
        assertThat(iterable.iterator().hasNext(), is(false));
    }
    
    @Test public void
    convertsAListOfRefsIntoAnIterable() {
        @SuppressWarnings("unchecked")
        Iterable<String> iterable = Refs.toIterable(Lists.newArrayList(Cell.of("Hello"),
                                                                       Cell.<String>of(null),
                                                                       Cell.of("World")));
        assertThat(Lists.newArrayList(iterable), contains("Hello", "World"));
    }
    
}
