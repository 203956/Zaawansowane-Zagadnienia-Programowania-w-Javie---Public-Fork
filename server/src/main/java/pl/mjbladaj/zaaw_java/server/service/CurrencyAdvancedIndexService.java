package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;

import java.util.List;

public interface CurrencyAdvancedIndexService {
    Index getCurrencyAdvancedIndex(List<UniversalCurrencyRateInTime> firstListOfCurrencyPrices, List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices, List<Double> firstListOfCurrencyAmounts, List<Double> secondListOfCurrencyAmounts );
}
