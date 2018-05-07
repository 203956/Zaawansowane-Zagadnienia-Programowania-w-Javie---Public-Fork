package pl.mjbladaj.zaaw_java.server;

import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.List;

public class ClassMatcher implements ArgumentMatcher<Class> {
    private List<Class> currencies;

    public ClassMatcher(Class... currencies) {
        this.currencies = Arrays.asList(currencies);
    }

    @Override
    public boolean matches(Class c) {
        return currencies.contains(c);
    }
}
