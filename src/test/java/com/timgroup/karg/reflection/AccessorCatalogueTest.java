package com.timgroup.karg.reflection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import org.hamcrest.Matchers;
import org.junit.Test;

public class AccessorCatalogueTest {

    public static class TestClass {
        public final String readOnlyField = "read-only value 1";
        public String getReadOnlyProperty() {
            return "read-only value 2";
        }
        public String writableField = "writable value 1";
        
        private String encapsulatedField = "writable value 2";
        public String getEncapsulatedField() {
            return encapsulatedField;
        }
        
        public void setEncapsulatedField(String value) {
            encapsulatedField = value;
        }
    }
    
    @Test public void
    exposes_all_attributes_of_a_class_as_getters() {
        AccessorCatalogue<TestClass> catalogue = AccessorCatalogue.forClass(TestClass.class);
        assertThat(catalogue.readOnlyAttributes().keySet(),
                   hasItems("readOnlyField", "readOnlyProperty", "writableField", "encapsulatedField",
                            "hashCode", "class", "toString"));
    }
    
    @Test public void
    exposes_all_writable_attributes_of_a_class_as_lenses() {
        AccessorCatalogue<TestClass> catalogue = AccessorCatalogue.forClass(TestClass.class);
        assertThat(catalogue.allAttributes().keySet(),
                   hasItems("writableField", "encapsulatedField"));
    }
    
    @Test public void
    does_not_expose_read_only_attributes_of_a_class_as_lenses() {
        AccessorCatalogue<TestClass> catalogue = AccessorCatalogue.forClass(TestClass.class);
        assertThat(catalogue.allAttributes().keySet(),
                   Matchers.not(hasItems("readOnlyField", "readOnlyProperty",
                                         "hashCode", "class", "toString")));
    }
}
