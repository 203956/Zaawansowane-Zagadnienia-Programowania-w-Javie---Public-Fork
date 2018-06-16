package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.dto.KindOfIndex;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.List;

public interface AmountAdvancedIndexService {
    Index getCurrencyAmountAdvancedIndex(String currencySymbol, String startDate, String endDate, List<UniversalCurrencyRateInTime> firstListOfCurrencyPrices, List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices, List<Double> firstListOfCurrencyAmounts, List<Double> secondListOfCurrencyAmounts, KindOfIndex kind)  throws TimePeriodNotAvailableException, EntityNotFoundException;
}
