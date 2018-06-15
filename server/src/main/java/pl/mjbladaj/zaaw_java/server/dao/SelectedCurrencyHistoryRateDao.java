package pl.mjbladaj.zaaw_java.server.dao;

import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface SelectedCurrencyHistoryRateDao {
    UniversalCurrencyRateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date) throws TimePeriodNotAvailableException, EntityNotFoundException;
    List<UniversalCurrencyRateInTime> getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate) throws TimePeriodNotAvailableException, EntityNotFoundException;

}
