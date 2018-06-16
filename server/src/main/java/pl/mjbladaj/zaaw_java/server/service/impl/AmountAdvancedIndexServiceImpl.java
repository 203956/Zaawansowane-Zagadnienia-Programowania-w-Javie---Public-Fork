package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.dto.KindOfIndex;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AmountAdvancedIndexService;
import pl.mjbladaj.zaaw_java.server.service.CurrencyAdvancedIndexService;

import java.util.List;

@Service
public class AmountAdvancedIndexServiceImpl implements AmountAdvancedIndexService {


    @Autowired
    AmountAdvancedIndexService amountAdvancedIndexService;


    @Autowired
    CurrencyAdvancedIndexService currencyAdvancedIndex;

    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Autowired
    private Environment environment;


    @Override
    public Index getCurrencyAmountAdvancedIndex(String currencySymbol, String startDate, String endDate, List<UniversalCurrencyRateInTime> firstListOfCurrencyPrices,
                                                     List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices, List<Double> firstListOfCurrencyAmounts, List<Double> secondListOfCurrencyAmounts,
                                                     KindOfIndex kind) throws TimePeriodNotAvailableException, EntityNotFoundException {

        String toCurrency= environment.getProperty("exchange.currency.default");

        switch (kind) {
            case Paasche:
                firstListOfCurrencyPrices = secondListOfCurrencyPrices = selectedCurrencyHistoryRateDao.getGivenPeriodRate(currencySymbol,toCurrency ,endDate,endDate );
                break;

            case Laspeyres:
                firstListOfCurrencyPrices = secondListOfCurrencyPrices = selectedCurrencyHistoryRateDao.getGivenPeriodRate(currencySymbol,toCurrency ,startDate,startDate );
                break;
            default:
                break;
        }

        return currencyAdvancedIndex.getCurrencyAdvancedIndex(firstListOfCurrencyPrices, secondListOfCurrencyPrices, firstListOfCurrencyAmounts, secondListOfCurrencyAmounts);
    }
}
