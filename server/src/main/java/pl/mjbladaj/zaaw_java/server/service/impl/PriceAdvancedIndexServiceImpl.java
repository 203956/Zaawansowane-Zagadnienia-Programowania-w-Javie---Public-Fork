package pl.mjbladaj.zaaw_java.server.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.CurrencyAdvancedIndexService;
import pl.mjbladaj.zaaw_java.server.service.PriceAdvancedIndexService;
import org.springframework.core.env.Environment;

import java.util.List;

@Service
public class PriceAdvancedIndexServiceImpl implements PriceAdvancedIndexService {
    @Autowired
    CurrencyAdvancedIndexService currencyAdvancedIndex;

    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Autowired
    private Environment environment;


    @Override
    public Index getCurrencyPriceAmountAdvancedIndex(String currencySymbol, String startDate, String endDate, List<UniversalCurrencyRateInTime> firstListOfCurrencyPrices,
                                                List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices, List<Double> firstListOfCurrencyAmounts, List<Double> secondListOfCurrencyAmounts) throws  TimePeriodNotAvailableException, EntityNotFoundException {

        String toCurrency= environment.getProperty("exchange.currency.default");

        firstListOfCurrencyPrices = selectedCurrencyHistoryRateDao.getGivenPeriodRate(currencySymbol,toCurrency ,endDate,endDate );
        secondListOfCurrencyPrices = selectedCurrencyHistoryRateDao.getGivenPeriodRate(currencySymbol, toCurrency,startDate,startDate );

        return currencyAdvancedIndex.getCurrencyAdvancedIndex(firstListOfCurrencyPrices, secondListOfCurrencyPrices, firstListOfCurrencyAmounts, secondListOfCurrencyAmounts);
    }
}
