package pl.mjbladaj.zaaw_java.server.service.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.RateConverter;
import pl.mjbladaj.zaaw_java.server.converters.RateInTimeConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.dto.Rate;
import pl.mjbladaj.zaaw_java.server.dto.RateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Override
    public CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException {
        Rate rate = selectedCurrencyRateDao.getRate(fromCurrency, toCurrency);

        if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        return  RateConverter.getCurrencyRate(rate, fromCurrency + "_" + toCurrency);
    }

    @Override
    public CurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws TimePeriodNotAvailableException, EntityNotFoundException {
        RateInTime rate = selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, date);

        if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        return  RateInTimeConverter.getCurrencyRateInTime(rate, fromCurrency + "_" + toCurrency, date);
    }

    @Override
    public List<CurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws TimePeriodNotAvailableException, EntityNotFoundException {
        List<RateInTime>  rate = selectedCurrencyHistoryRateDao.getGivenPeriodRate(fromCurrency, toCurrency, startDay, endDay);

        if (rate.isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        return  RateInTimeConverter.getCurrenciesRateInTime(rate, fromCurrency + "_" + toCurrency);
    }

    @Override
    public List<CurrencyRateInTime> getDifferenceInRatesRatesForGivenPeriod(String fromCurrency, String symbol1, String symbol2, String startDay, String endDay) throws TimePeriodNotAvailableException {
        List<RateInTime>  rateFirstCurrency = selectedCurrencyHistoryRateDao.getGivenPeriodRate(symbol1, fromCurrency, startDay, endDay);
        List<CurrencyRateInTime> resultFirstCurrency = RateInTimeConverter.getCurrenciesRateInTime(rateFirstCurrency, fromCurrency + "_" + symbol1);

        List<RateInTime>  rateSecondCurrency = selectedCurrencyHistoryRateDao.getGivenPeriodRate(symbol2, fromCurrency, startDay, endDay);
        List<CurrencyRateInTime> resultSecondCurrency = RateInTimeConverter.getCurrenciesRateInTime(rateSecondCurrency, fromCurrency + "_" + symbol2);

        return mergeResults(resultFirstCurrency, resultSecondCurrency);
    }

    private List<CurrencyRateInTime> mergeResults(List<CurrencyRateInTime> resultFirstCurrency, List<CurrencyRateInTime> resultSecondCurrency) {
        List<CurrencyRateInTime> result = new ArrayList<>();

        for (int i = 0; i < resultFirstCurrency.size(); i++) {
            CurrencyRateInTime currencyRateInTime = new CurrencyRateInTime();
            currencyRateInTime.setTime(resultFirstCurrency.get(i).getTime());
            CurrencyRateInTime el = resultSecondCurrency.get(i);
            currencyRateInTime.setRate(resultFirstCurrency.get(i).getRate() - el.getRate() );
            result.add(currencyRateInTime);
        }
        return result;
    }

}
