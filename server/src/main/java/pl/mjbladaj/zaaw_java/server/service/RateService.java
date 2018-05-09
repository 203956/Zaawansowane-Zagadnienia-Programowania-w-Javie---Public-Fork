package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface RateService {
    CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException, CurrencyNotAvailableException;
    UniversalCurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws EntityNotFoundException, TimePeriodNotAvailableException;
    List<UniversalCurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws EntityNotFoundException, TimePeriodNotAvailableException;
    List<UniversalCurrencyRateInTime> getDifferenceInRatesRatesForGivenPeriod(String fromCurrency, String symbol1, String symbol2, String startDay, String endDay) throws EntityNotFoundException, TimePeriodNotAvailableException;

}
