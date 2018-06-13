package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRate;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;

public abstract class UniversalRateConverter {
    public static UniversalRate getCurrencyRate(FreeCurrenciesComRate freeCurrenciesComRate, String fromCurrency, String toCurrency) throws EntityNotFoundException {
        checkRequestResults(freeCurrenciesComRate);

        return UniversalRate
                .builder()
                .symbol(fromCurrency)
                .rate((Number) freeCurrenciesComRate.getResults().
                        get(getKey(fromCurrency, toCurrency) ).get("val"))
                .build();
    }
    private static String getKey(String fromCurrency, String toCurrency) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(fromCurrency);
        stringBuilder.append("_");
        stringBuilder.append(toCurrency);

        return stringBuilder.toString();
    }
    private static void checkRequestResults(FreeCurrenciesComRate freeCurrenciesComRate) throws EntityNotFoundException {
        if (freeCurrenciesComRate.getQuery().isEmpty() || freeCurrenciesComRate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exists.");
    }
}
