package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface HistoricalRateService {
    UniversalCurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws EntityNotFoundException, TimePeriodNotAvailableException, CurrencyNotAvailableException;
    List<UniversalCurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws EntityNotFoundException, TimePeriodNotAvailableException, CurrencyNotAvailableException;

}
