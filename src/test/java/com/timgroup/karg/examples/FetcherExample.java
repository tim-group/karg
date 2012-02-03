package com.timgroup.karg.examples;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.timgroup.karg.functions.Curry;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.keywords.Keywords;
import com.timgroup.karg.multipledispatch.CandidateFunctionRegistry;
import com.timgroup.karg.multipledispatch.FunctionBundle;
import com.timgroup.karg.multipledispatch.ParameterMatching;


public class FetcherExample {
    
    // Some parameters to use in specifying queries
    public static final Keyword<String> name = Keywords.newKeyword();
    public static final Keyword<String> id = Keywords.newKeyword();
    public static final Keyword<String> company = Keywords.newKeyword();
    
    // A dummy entity class
    public static class FundOfFundOfFund {
        public final String name;
        public final String id;
        public final String companyName;
        
        public FundOfFundOfFund(String name, String id, String companyName) {
            this.name = name;
            this.id = id;
            this.companyName = companyName;
        }
    }
    
    // Universal fetcher interface
    public static interface Fetcher<T> {
        
        Iterator<T> fetchCursor(KeywordArgument...parameters);
        List<T> fetchAll(KeywordArgument...parameters);
        T fetchOne(KeywordArgument...parameters);
        boolean exists(KeywordArgument...parameters);
        
    }
    
    // Shorthand
    public static interface FetcherFunction<T> extends Function<KeywordArguments, Iterator<T>> { }
    public static interface FetcherFunctionBundle<T> extends FunctionBundle<Iterator<T>> { }

    // Adapter from a fetcher function to a fetcher
    public static class FetcherFunctionAdapter<T> implements Fetcher<T> {
        public static <T> Fetcher<T> adapt(Function<KeywordArguments, Iterator<T>> function) {
            return new FetcherFunctionAdapter<T>(function);
        }
        
        private FetcherFunctionAdapter(Function<KeywordArguments, Iterator<T>> function) {
            this.function = function;
        }
        
        private final Function<KeywordArguments, Iterator<T>> function;

        @Override
        public Iterator<T> fetchCursor(KeywordArgument... parameters) {
            return function.apply(KeywordArguments.of(parameters));
        }

        @Override
        public List<T> fetchAll(KeywordArgument...parameters) {
            return newArrayList(fetchCursor(parameters));
        }
        
        @Override
        public T fetchOne(KeywordArgument...parameters) {
            Iterator<T> cursor = fetchCursor(parameters); 
            return cursor.hasNext() ? cursor.next() : null;
        }
        
        @Override
        public boolean exists(KeywordArgument...parameters) {
            return fetchCursor(parameters).hasNext();
        }
    }
    
    // Our pretend database
    private static final List<FundOfFundOfFund> allFofofs = newArrayList();
    
    // A pretend table query
    private static Iterator<FundOfFundOfFund> filteredFofofs(Predicate<FundOfFundOfFund> predicate) {
        return filter(allFofofs, predicate).iterator();
    }
    
    // Factory for a Fetcher of FundOfFundOfFunds
    public static class FundOfFundOfFundFetcherFactory {
        
        private FundOfFundOfFundFetcherFactory() { }
        
        public static Fetcher<FundOfFundOfFund> create(String defaultCompanyName) {
            ParameterMatching<Iterator<FundOfFundOfFund>> fetcher = ParameterMatching.function(fetcherFunctions);
            
            return FetcherFunctionAdapter.adapt(Curry.theFunction(fetcher).with(company.of(defaultCompanyName)));
        }
        
        private static final FetcherFunctionBundle<FundOfFundOfFund> fetcherFunctions = new FetcherFunctionBundle<FundOfFundOfFund>() {
            @Override
            public void register(CandidateFunctionRegistry<Iterator<FundOfFundOfFund>> registry) {
                registry.match(KeywordArguments.containing(name, company)).with(new FetchByNameAndCompany());
                registry.match(KeywordArguments.containing(id, company)).with(new FetchByIdAndCompany());
                registry.match(KeywordArguments.containing(company)).with(new FetchByCompany());
            }
        };
      
        private static abstract class FundOfFundOfFundFetcherFunction implements FetcherFunction<FundOfFundOfFund> {
            @Override public Iterator<FundOfFundOfFund> apply(final KeywordArguments parameters) {
                return filteredFofofs(new Predicate<FundOfFundOfFund>() {
                    @Override
                    public boolean apply(FundOfFundOfFund input) {
                        return matches(input, parameters);
                    }
                });
            }
            
            protected abstract boolean matches(FundOfFundOfFund input, KeywordArguments parameters);
        }
        
        private static class FetchByNameAndCompany extends FundOfFundOfFundFetcherFunction {
            @Override protected boolean matches(FundOfFundOfFund input, KeywordArguments parameters) {
                return company.from(parameters).equals(input.companyName)
                    && name.from(parameters).equals(input.name);
            }
        }
        
        private static class FetchByIdAndCompany extends FundOfFundOfFundFetcherFunction {
            @Override protected boolean matches(FundOfFundOfFund input, KeywordArguments parameters) {
                return company.from(parameters).equals(input.companyName)
                    && id.from(parameters).equals(input.id);
            }
        }
        
        private static class FetchByCompany extends FundOfFundOfFundFetcherFunction {
            @Override protected boolean matches(FundOfFundOfFund input, KeywordArguments parameters) {
                return company.from(parameters).equals(input.companyName);
            }
        }
    }
    
    // A test bringing it all together.
    @Test public void
    fetchFofofsByVariousCriteria() {
        FundOfFundOfFund fofof1 = new FundOfFundOfFund("Super fund", "FOFOF1", "Banana co");
        FundOfFundOfFund fofof2 = new FundOfFundOfFund("Mega fund", "FOFOF2", "Murder inc");
        FundOfFundOfFund fofof3 = new FundOfFundOfFund("Giga fund", "FOFOF3", "Banana co");
        
        allFofofs.add(fofof1);
        allFofofs.add(fofof2);
        allFofofs.add(fofof3);
        
        Fetcher<FundOfFundOfFund> fetcher = FundOfFundOfFundFetcherFactory.create("Banana co");

        // We can fetch a FOFOF by name
        assertThat(fetcher.fetchOne(name.of("Super fund")), is(fofof1));
        
        // or by id
        assertThat(fetcher.fetchOne(id.of("FOFOF3")), is(fofof3));
        
        // but not if it belongs to a different company
        assertThat(fetcher.exists(id.of("FOFOF2")), is(false));
        assertThat(fetcher.fetchAll().size(), is(2));
        
        // although this can be overridden if necessary
        assertThat(fetcher.exists(id.of("FOFOF2"), company.of("Murder inc")), is(true));
    }
    
}
