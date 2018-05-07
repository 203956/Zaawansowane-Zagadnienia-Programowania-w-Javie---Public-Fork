package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface RateService {
    CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException;
}
