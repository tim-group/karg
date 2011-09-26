package com.timgroup.karg;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.reference.Getters;
import com.timgroup.karg.reference.Lens;
import com.timgroup.karg.reference.Lenses;
import com.timgroup.karg.reference.Ref;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.transform;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class LensesTest {
    
    private static final Keyword<String> NAME = Keyword.newKeyword();
    private static final Keyword<KeywordArguments> ADDRESS = Keyword.newKeyword();
    private static final Keyword<String> FIRST_LINE = Keyword.newKeyword();
    private static final Keyword<String> SECOND_LINE = Keyword.newKeyword();
    private static final Keyword<String> POSTCODE = Keyword.newKeyword();
    
    private final KeywordArguments arthursDetails = KeywordArguments.of(
                                                        NAME.of("Arthur Putey"),
                                                        ADDRESS.of(KeywordArguments.of(FIRST_LINE.of("123 Penguin Drive"),
                                                                                       SECOND_LINE.of("Coventry"),
                                                                                       POSTCODE.of("VB6 5UX"))));
    
    private final KeywordArguments marthasDetails = KeywordArguments.of(
                                                        NAME.of("Martha Wainscot"),
                                                        ADDRESS.of(KeywordArguments.of(FIRST_LINE.of("22 Acacia Avenue"),
                                                                                       SECOND_LINE.of("Hull"),
                                                                                       POSTCODE.of("HU1 1UH"))));
    
    @Test public void
    lensesCompose() {
        Lens<KeywordArguments, String> FIRST_LINE_OF_ADDRESS = Getters.compose(ADDRESS, FIRST_LINE);
        
        assertThat(FIRST_LINE_OF_ADDRESS.get(arthursDetails), is("123 Penguin Drive"));
    }
    
    @Test public void
    lensesComposeWithPredicates() {
        Predicate<KeywordArguments> isArthur = Lenses.compose(NAME, equalTo("Arthur Putey"));
        
        assertThat(isArthur.apply(arthursDetails), is(true));
        assertThat(isArthur.apply(marthasDetails), is(false));
    }
    
    @Test public void
    lensesAdaptToFunctions() {
        Function<KeywordArguments, String> getFirstLine = Lenses.toFunction(FIRST_LINE);
        
        assertThat(getFirstLine.apply(ADDRESS.from(arthursDetails)), is("123 Penguin Drive"));
    }
    
    @Test public void
    lensesComposeWithFunctions() {
        Function<String, String> toLower = new Function<String, String>() {
            @Override public String apply(String arg0) {
                return arg0.toLowerCase();
            }
        };
        Function<KeywordArguments, String> lowercaseName = Lenses.compose(NAME, toLower);
        
        assertThat(lowercaseName.apply(arthursDetails), is("arthur putey"));
    }
    
    
    @Test public void
    lensesProject() {
        List<KeywordArguments> people = Lists.newArrayList(arthursDetails, marthasDetails);
        
        assertThat(transform(people, Getters.toFunction(NAME)), Matchers.contains("Arthur Putey", "Martha Wainscot"));
    }
    
   
    
    @Test public void
    providesLensIntoFirstListItem() {
        List<String> names = Lists.newArrayList("Matthew", "Mark", "Luke", "Rufus");
        assertThat(Lenses.<String>firstItem().get(names), is("Matthew"));
    }
    
    @Test public void
    providesLensIntoIndexedListItem() {
        List<String> names = Lists.newArrayList("Luke", "Han", "Leia", "Chewbacca");
        assertThat(Lenses.<String>atIndex(2).get(names), is("Leia"));
    }
    
    @Test public void
    providesLensToFirstMatchingListItem() {
        List<String> names = Lists.newArrayList("Ice Cube", "Ice-T", "Vanilla Ice");
        Predicate<String> isVanilla = new Predicate<String>() {
            @Override
            public boolean apply(String arg0) {
                return arg0.contains("Vanilla");
            }
        };
        assertThat(Lenses.firstMatch(isVanilla).get(names), is("Vanilla Ice"));
    }
    
    @Test public void
    aBoundLensIsARef() {
        List<String> names = Lists.newArrayList("Booch", "Jacobson", "Rumbaugh");
        Ref<String> firstName = Lenses.bind(Lenses.<String>firstItem()).to(names);
        firstName.set("Cletus");
        assertThat(names.get(0), is("Cletus"));
    }
    
}
