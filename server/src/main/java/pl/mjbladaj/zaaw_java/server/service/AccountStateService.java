package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.exceptions.AccountNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;

import java.util.Map;

public interface AccountStateService {
    void addMoneyToAccount(String username, String currency, double amount) throws CurrencyNotAvailableException;
    Map<String, Double> getUserAccountState(String username);
}
