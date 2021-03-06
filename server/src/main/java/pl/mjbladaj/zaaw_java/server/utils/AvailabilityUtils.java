package pl.mjbladaj.zaaw_java.server.utils;

import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;

public class AvailabilityUtils {

    public static void checkAvailability(AvailableCurrenciesService availableCurrenciesService,
                                          String... currency) throws CurrencyNotAvailableException {
        for (String element: currency) {
            checkAvailability(availableCurrenciesService, element);
        }
    }
    public static void checkAvailability(AvailableCurrenciesService availableCurrenciesService,
                                         String currency) throws CurrencyNotAvailableException {
        if(!availableCurrenciesService.isAvailable(currency).isAvailability() ) {
            throw new CurrencyNotAvailableException("Currency is not available.");
        }
    }
}
