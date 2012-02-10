package com.timgroup.karg.naming;

import static com.timgroup.karg.naming.TargetNameFormatter.LOWER_CAMEL_CASE;
import static com.timgroup.karg.naming.TargetNameFormatter.UNDERSCORE_SEPARATED;
import static com.timgroup.karg.naming.TargetNameFormatter.UPPER_CAMEL_CASE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class TargetNameFormatterTest {

    @Test public void
    converts_underscored_name_to_lower_camel_case() {
        assertThat(UNDERSCORE_SEPARATED.parse("foo__bar__baz_")
                                       .formatWith(LOWER_CAMEL_CASE),
                   is("fooBarBaz"));
    }
    
    @Test public void
    converts_underscored_name_to_upper_camel_case() {
        assertThat(UNDERSCORE_SEPARATED.parse("foo__bar__baz_")
                                       .formatWith(UPPER_CAMEL_CASE),
                   is("FooBarBaz"));
    }
    
    @Test public void
    converts_upper_camel_cased_name_to_underscored() {
        assertThat(UPPER_CAMEL_CASE.parse("FooBarBaz")
                                   .formatWith(UNDERSCORE_SEPARATED),
                   is("foo_bar_baz"));
    }
    
    @Test public void
    converts_lower_camel_cased_name_to_underscored() {
        assertThat(UPPER_CAMEL_CASE.parse("fooBarBaz")
                                   .formatWith(UNDERSCORE_SEPARATED),
                   is("foo_bar_baz"));
    }
}
