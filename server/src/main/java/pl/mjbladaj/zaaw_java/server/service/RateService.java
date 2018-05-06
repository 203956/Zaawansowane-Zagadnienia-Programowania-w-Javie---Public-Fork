package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface RateService {
    CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException;
    CurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws EntityNotFoundException, TimePeriodNotAvailableException;
    List<CurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws EntityNotFoundException, TimePeriodNotAvailableException;
    List<CurrencyRateInTime> getDifferenceInRatesRatesForGivenPeriod(String fromCurrency, String symbol1, String symbol2, String startDay, String endDay) throws EntityNotFoundException, TimePeriodNotAvailableException;
}
