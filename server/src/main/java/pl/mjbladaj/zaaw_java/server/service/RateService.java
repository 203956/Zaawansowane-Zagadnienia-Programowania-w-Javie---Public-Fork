package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.SameCurrenciesConvertException;

import java.util.ArrayList;

public interface RateService {
    CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException, CurrencyNotAvailableException;
    CurrencyRate getAmountOfAnotherCurrency(ArrayList<Integer> amountOfInCurrencies, ArrayList<String> inCurrencies, String outCurrency) throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException;
}
