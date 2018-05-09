package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateCalculationsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricalRateCalculationsImpl implements HistoricalRateCalculationsService {

    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Override
    public List<UniversalCurrencyRateInTime> getDifferenceInRatesRatesForGivenPeriod(String fromCurrency, String symbol1, String symbol2, String startDay, String endDay) throws TimePeriodNotAvailableException, EntityNotFoundException {
        List<UniversalCurrencyRateInTime>  rateFirstCurrency = selectedCurrencyHistoryRateDao.getGivenPeriodRate(symbol1, fromCurrency, startDay, endDay);
        List<UniversalCurrencyRateInTime>  rateSecondCurrency = selectedCurrencyHistoryRateDao.getGivenPeriodRate(symbol2, fromCurrency, startDay, endDay);

        return mergeResults(rateFirstCurrency, rateSecondCurrency);
    }

    private List<UniversalCurrencyRateInTime> mergeResults(List<UniversalCurrencyRateInTime> resultFirstCurrency, List<UniversalCurrencyRateInTime> resultSecondCurrency) {
        List<UniversalCurrencyRateInTime> result = new ArrayList<>();

        for (int i = 0; i < resultFirstCurrency.size(); i++) {
            UniversalCurrencyRateInTime universalCurrencyRateInTime = new UniversalCurrencyRateInTime();
            universalCurrencyRateInTime.setTime(resultFirstCurrency.get(i).getTime());
            UniversalCurrencyRateInTime el = resultSecondCurrency.get(i);
            universalCurrencyRateInTime.setRate(resultFirstCurrency.get(i).getRate() - el.getRate() );
            result.add(universalCurrencyRateInTime);
        }
        return result;
    }

}

