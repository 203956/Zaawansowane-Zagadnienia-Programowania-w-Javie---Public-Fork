package pl.mjbladaj.zaaw_java.server.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.RateConverter;
import pl.mjbladaj.zaaw_java.server.converters.RateInTimeConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Override
    public CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException, CurrencyNotAvailableException {
        checkAvability(fromCurrency, toCurrency);
        UniversalRate rate = selectedCurrencyRateDao.getRate(fromCurrency, toCurrency);
        return RateConverter.getCurrencyRate(rate);
    }

    private void checkAvability(String fromCurrency, String toCurrency) throws CurrencyNotAvailableException {
        if(!availableCurrenciesService.isAvailable(toCurrency).isAvailability() ||
                !availableCurrenciesService.isAvailable(fromCurrency).isAvailability()) {
            throw new CurrencyNotAvailableException("Currency is not available.");
        }
    }

    @Override
    public UniversalCurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws TimePeriodNotAvailableException, EntityNotFoundException {

        return  selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, date);
    }

    @Override
    public List<UniversalCurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws TimePeriodNotAvailableException, EntityNotFoundException {
        return selectedCurrencyHistoryRateDao.getGivenPeriodRate(fromCurrency, toCurrency, startDay, endDay);

    }

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
