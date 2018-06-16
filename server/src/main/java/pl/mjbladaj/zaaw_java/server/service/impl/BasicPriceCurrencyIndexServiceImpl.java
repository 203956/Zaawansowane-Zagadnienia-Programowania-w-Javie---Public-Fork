package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.BasicPriceCurrencyIndexService;
import pl.mjbladaj.zaaw_java.server.dto.Index;

@Service
public class BasicPriceCurrencyIndexServiceImpl implements BasicPriceCurrencyIndexService {

    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Autowired
    private BasicPriceCurrencyIndexService basicPriceCurrencyIndexService;
    @Autowired
    private Environment environment;

    @Override
    public Index getBasicPriceCurrencyForGivenCurrencyInPeriod(String currencySymbol, String startDate, String endDate) throws TimePeriodNotAvailableException, EntityNotFoundException{
        String toCurrency = environment.getProperty("exchange.currency.default");
        UniversalCurrencyRateInTime universalCurrencyRateInStartTime = selectedCurrencyHistoryRateDao.getGivenDayRate(currencySymbol,toCurrency , startDate);
        UniversalCurrencyRateInTime universalCurrencyRateInEndTime = selectedCurrencyHistoryRateDao.getGivenDayRate(currencySymbol,toCurrency , endDate);
        Double quotient = (Double)(universalCurrencyRateInStartTime.getRate()/universalCurrencyRateInEndTime.getRate());
        return new Index(quotient);
    }
}
