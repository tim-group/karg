package com.timgroup.karg.examples;

import static com.timgroup.karg.keywords.Keywords.DISPLAY_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.keywords.Keywords;


public class RecordTypeExample {
    
    private static abstract class RecordType {
        
        // Metadata keywords used to tag keywords that will identify data columns used by a record type.
        public static final Keyword<String> COLUMN_NAME = Keywords.newKeyword();
        public static final Keyword<Matcher<? super String>> VALIDATOR = Keywords.newKeyword();
        
        private final KeywordArguments values;
        
        public RecordType(List<Keyword<String>> keys, Map<String, String> rawData) {
            values = convertStringMap(keys, rawData);
        }
        
        private KeywordArguments convertStringMap(List<Keyword<String>> keywords, Map<String, String> stringMap) {
            List<KeywordArgument> arguments = Lists.newArrayList();
            for (Keyword<String> keyword : keywords) {
                arguments.add(keyword.of(stringMap.get(COLUMN_NAME.from(keyword.metadata()))));
            }
            return KeywordArguments.of(arguments);
        }
        
        public <T> T get(Keyword<T> keyword) {
            return keyword.get(values);
        }
        
        public <T> T set(Keyword<T> keyword, T newValue) {
            return keyword.set(values, newValue);
        }
        
        private boolean validate(Keyword<?> keyword, KeywordArguments arguments) {
            return VALIDATOR.from(keyword.metadata()).matches(keyword.from(arguments));
        }
        
        public boolean validateAll() {
            for (Keyword<?> keyword : values.keySet()) {
                if (!validate(keyword, values)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private static final class TestRecordType extends RecordType {
        
        // Keywords identifying fields of this record type
        public static final Keyword<String> ALT_REF = Keywords.newKeyword(
                                                          DISPLAY_NAME.of("Alternative Reference"),
                                                          COLUMN_NAME.of("alt ref"),
                                                          VALIDATOR.of(Matchers.startsWith("REF-")));
        
        public static final Keyword<String> PUBLIC_ID = Keywords.newKeyword(
                                                            DISPLAY_NAME.of("Public ID"),
                                                            COLUMN_NAME.of("id"),
                                                            VALIDATOR.of(Matchers.startsWith("ID:")));
        
        public static final List<Keyword<String>> keys = Lists.newArrayList();
        static {
            keys.add(PUBLIC_ID);
            keys.add(ALT_REF);
        }
        
        public TestRecordType(Map<String, String> rawData) {
            super(keys, rawData);
        }
    }
    
    // Convert a set of name/value string pairs into a validatable record type with keyword getters
    @Test public void
    keywordsAreLensesIntoRecordTypes() {
        
        Map<String, String> invalidData = Maps.newHashMap();
        invalidData.put("alt ref", "REF-123");
        invalidData.put("id", "ID:123");
        
        Map<String, String> validData = Maps.newHashMap();
        validData.put("alt ref", "REF-123");
        validData.put("id", "ID:123");
                
        TestRecordType invalidRecord = new TestRecordType(invalidData);
        TestRecordType validRecord = new TestRecordType(validData);
        
        assertThat(TestRecordType.ALT_REF.metadata().valueOf(RecordType.COLUMN_NAME), is("alt ref"));
        
        assertThat(invalidRecord.get(TestRecordType.PUBLIC_ID), is("ID:123"));
        assertThat(invalidRecord.get(TestRecordType.ALT_REF), is("REF-123"));
        assertThat(invalidRecord.validateAll(), is(true));
        
        invalidRecord.set(TestRecordType.ALT_REF, "REF:123");
        assertThat(invalidRecord.get(TestRecordType.ALT_REF), is("REF:123"));
        
        assertThat(invalidRecord.validateAll(), is(false));
        assertThat(validRecord.validateAll(), is(true));
    }

}
