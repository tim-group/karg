package com.timgroup.karg.valuetypes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.timgroup.karg.keywords.typed.TypedKeyword;
import com.timgroup.karg.keywords.typed.TypedKeywordArguments;
import com.timgroup.karg.keywords.typed.TypedKeywords;

public class ValueTypesTest {

    public static class Person extends ValueType<Person> {
        public static final TypedKeyword<Person, String> NAME = TypedKeywords.newTypedKeyword();
        public final String name;
        
        public static final TypedKeyword<Person, String> BIRTHDAY = TypedKeywords.newTypedKeyword();
        public final String birthday;

        public static final TypedKeyword<Person, Integer> AGE = TypedKeywords.newTypedKeyword();
        public final int age;
        
        public Person(TypedKeywordArguments<Person> args) {
            super(args);
            name = NAME.from(args);
            birthday = BIRTHDAY.from(args);
            age = AGE.from(args);
        }
    }
    
    @SuppressWarnings("unchecked")
    private final Person peter = new Person(TypedKeywordArguments.of(
            Person.NAME.of("Peter"),
            Person.BIRTHDAY.of("12th October"),
            Person.AGE.of(20)));
    
    @SuppressWarnings("unchecked")
    private final Person paul = new Person(TypedKeywordArguments.of(
            Person.NAME.of("Paul"),
            Person.BIRTHDAY.of("6th June"),
            Person.AGE.of(22)));
    
    @SuppressWarnings("unchecked")
    private final Person mary = new Person(TypedKeywordArguments.of(
            Person.NAME.of("Mary"),
            Person.BIRTHDAY.of("21st February"),
            Person.AGE.of(21)));
    
    private final List<Person> people = Lists.newArrayList(peter, paul, mary);
    
    private static final Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) { return input + 1; }
    };
    
    private static final Function<Person, String> sayHappyBirthday = new Function<Person, String>() {
        @Override
        public String apply(Person input) { return String.format("Happy birthday %s, %d today!", input.name, input.age); }
    };
    
    @Test public void
    whose_birthday_is_it() {
        List<String> messages = ImmutableList.copyOf(
            Iterables.transform(Iterables.transform(Iterables.filter(people, ValueTypes.select(Person.BIRTHDAY, "6th June")),
                                                    ValueTypes.update(Person.AGE, addOne)),
                                sayHappyBirthday));
        
        assertThat(messages, contains("Happy birthday Paul, 23 today!"));
    }
}
