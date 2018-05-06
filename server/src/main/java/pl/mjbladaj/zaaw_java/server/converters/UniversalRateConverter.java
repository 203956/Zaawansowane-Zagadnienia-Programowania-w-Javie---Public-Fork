package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.dao.impl.models.Rate;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;

public abstract class UniversalRateConverter {
    public static UniversalRate getCurrencyRate(Rate rate, String fromCurrency, String toCurrency) throws EntityNotFoundException {
        checkReuestsResults(rate);

        return UniversalRate
                .builder()
                .symbol(fromCurrency)
                .rate((double) rate.getResults().
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
    private static void checkReuestsResults(Rate rate) throws EntityNotFoundException {
        if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exists.");
    }
}
