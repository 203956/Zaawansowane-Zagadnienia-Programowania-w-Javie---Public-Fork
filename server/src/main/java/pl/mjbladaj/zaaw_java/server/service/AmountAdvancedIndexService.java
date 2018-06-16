package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.Index;

import java.util.List;

public interface AmountAdvancedIndexService {
    Index getCurrencyAmountAdvancedIndex(List<Double> firstListOfCurrencyPrices, List<Double> secondListOfCurrencyPrices, List<Double> firstListOfCurrencyAmounts, List<Double> secondListOfCurrencyAmounts );
}
