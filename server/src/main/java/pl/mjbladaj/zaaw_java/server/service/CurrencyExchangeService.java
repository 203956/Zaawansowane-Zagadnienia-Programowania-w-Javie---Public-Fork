package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.ExchangeStatus;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountStateException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;

public interface CurrencyExchangeService {
    ExchangeStatus exchange(String username, String fromCurrency, String toCurrency, double amount)
            throws CurrencyNotAvailableException, AccountStateException, EntityNotFoundException;
}
