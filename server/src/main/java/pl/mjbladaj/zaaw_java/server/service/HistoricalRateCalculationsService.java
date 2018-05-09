package pl.mjbladaj.zaaw_java.server.service;

import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface HistoricalRateCalculationsService {
    List<UniversalCurrencyRateInTime> getDifferenceInRatesRatesForGivenPeriod(String fromCurrency, String symbol1, String symbol2, String startDay, String endDay) throws EntityNotFoundException, TimePeriodNotAvailableException;

}
