package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;

public interface AccountStateService {
    void addMoneyToAccount(String username, String currency, double amount) throws CurrencyNotAvailableException;
}
