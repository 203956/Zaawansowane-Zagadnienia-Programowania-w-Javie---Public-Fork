package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.service.AmountAdvancedIndexService;
import pl.mjbladaj.zaaw_java.server.service.CurrencyAdvancedIndexService;

import java.util.List;

@Service
public class AmountAdvancedIndexServiceImpl implements AmountAdvancedIndexService {


    @Autowired
    AmountAdvancedIndexService amountAdvancedIndexService;


    @Override
    public Index getCurrencyAmountAdvancedIndex(List<Double> firstListOfCurrencyPrices, List<Double> secondListOfCurrencyPrices, List<Double> firstListOfCurrencyAmounts, List<Double> secondListOfCurrencyAmounts) {
        return null;
    }
}
