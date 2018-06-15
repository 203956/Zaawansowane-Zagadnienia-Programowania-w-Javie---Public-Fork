package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.exceptions.AccountStateException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;

public interface CurrencyExchangeService {
    void exchange(String username, String fromCurrency, String toCurrency, double amount)
            throws CurrencyNotAvailableException, AccountStateException;
}
