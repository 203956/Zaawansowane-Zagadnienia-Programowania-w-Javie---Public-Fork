package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.SameCurrenciesConvertException;

import java.util.ArrayList;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface RateService {
    CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException, CurrencyNotAvailableException;
    CurrencyRate getAmountOfAnotherCurrency(ArrayList<Double> amountOfInCurrencies, ArrayList<String> inCurrencies, String outCurrency) throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException;
}
