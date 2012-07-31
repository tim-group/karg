package com.timgroup.karg.naming;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.timgroup.karg.naming.StringFunctions.LOWERCASE_TRIM;
import static com.timgroup.karg.naming.StringFunctions.NOT_EMPTY;

public interface TargetNameParser {

    static TargetNameParser CAMEL_CASE = new CamelCaseParser();
    static TargetNameParser UNDERSCORE_SEPARATED = new UnderscoreParser();

    TargetName parse(String input);

    static final class UnderscoreParser implements TargetNameParser {
        @Override
        public TargetName parse(String input) {
            Iterator<String> words = transform(filter(newArrayList(input.split("_")).iterator(), NOT_EMPTY), LOWERCASE_TRIM);
            return new TargetName(Lists.newArrayList(words));
        }
    }
    
    final class CamelCaseParser implements TargetNameParser {
        
        public TargetName parse(String nameString) {
            StringBuilder word = new StringBuilder();
            List<String> words = newLinkedList();
            for(char c: nameString.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    if (word.length() >0) {
                        words.add(word.toString());
                        word = new StringBuilder();
                    }
                }
                word.append(c);
            }
            if (word.length() > 0) {
                words.add(word.toString());
            }
            return new TargetName(words);
        }
    }

}
