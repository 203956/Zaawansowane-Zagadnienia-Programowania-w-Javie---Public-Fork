package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.service.CurrencyAdvancedIndexService;
import java.util.List;

@Service
public class CurrencyAdvancedIndexServiceImpl implements CurrencyAdvancedIndexService {

    @Autowired
    private CurrencyAdvancedIndexService currencyAdvancedIndexService;


    @Override
    public Index getCurrencyAdvancedIndex(List<UniversalCurrencyRateInTime> firstListOfCurrencyPrices, List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices, List<Double> firstListOfCurrencyAmounts, List<Double> secondListOfCurrencyAmounts) {
      Double counter = 0.0, denominator = 0.0;

        for (int i = 0; i <firstListOfCurrencyPrices.size() ; i++) {

            counter += firstListOfCurrencyPrices.get(i).getRate() * firstListOfCurrencyAmounts.get(i);
            denominator+= secondListOfCurrencyPrices.get(i).getRate() * secondListOfCurrencyAmounts.get(i);
        }
        return new Index(counter/denominator);

    }

}
