package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.service.CurrencyAdvancedIndexService;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        Double quotient= counter/denominator;
        Double valueOfIndex = new BigDecimal(quotient.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return new Index(valueOfIndex);

    }

}
