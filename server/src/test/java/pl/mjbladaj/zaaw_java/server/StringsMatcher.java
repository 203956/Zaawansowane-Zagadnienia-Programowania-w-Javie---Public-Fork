package pl.mjbladaj.zaaw_java.server;

import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.List;

public class StringsMatcher implements ArgumentMatcher<String> {
    private List<String> currencies;

    public StringsMatcher(String... currencies) {
        this.currencies = Arrays.asList(currencies);
    }

    @Override
    public boolean matches(String s) {
        return currencies.contains(s);
    }
}