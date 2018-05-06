package pl.mjbladaj.zaaw_java.server.service.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.RateConverter;
import pl.mjbladaj.zaaw_java.server.converters.RateInTimeConverter;
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

    @Override
    public CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException {
        Rate rate = selectedCurrencyRateDao.getRate(fromCurrency, toCurrency);

        if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        return  RateConverter.getCurrencyRate(rate, fromCurrency + "_" + toCurrency);
    }

    @Override
    public CurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws TimePeriodNotAvailableException {
        RateInTime rate = selectedCurrencyRateDao.getGivenDayRate(fromCurrency, toCurrency, date);

       /* if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");*/

        return  RateInTimeConverter.getCurrencyRateInTime(rate, fromCurrency + "_" + toCurrency, date);
    }

    @Override
    public List<CurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws TimePeriodNotAvailableException {
        val rate = selectedCurrencyRateDao.getGivenPeriodRate(fromCurrency, toCurrency, startDay, endDay);

        /*if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");*/

        return  RateInTimeConverter.getCurrenciesRateInTime(rate, fromCurrency + "_" + toCurrency);
    }

    @Override
    public List<CurrencyRateInTime> getDifferenceInRatesRatesForGivenPeriod(String fromCurrency, String symbol1, String symbol2, String startDay, String endDay) throws TimePeriodNotAvailableException {
        val rate = selectedCurrencyRateDao.getGivenPeriodRate(fromCurrency, symbol1, startDay, endDay);
        List<CurrencyRateInTime> result1 = RateInTimeConverter.getCurrenciesRateInTime(rate, fromCurrency + "_" + symbol1);

        val rate2 = selectedCurrencyRateDao.getGivenPeriodRate(fromCurrency, symbol2, startDay, endDay);
        List<CurrencyRateInTime> result2 = RateInTimeConverter.getCurrenciesRateInTime(rate2, fromCurrency + "_" + symbol2);

        List<CurrencyRateInTime> result = new ArrayList<>();
        for (val elem: result1) {
            CurrencyRateInTime currencyRateInTime = new CurrencyRateInTime();
            currencyRateInTime.setTime(elem.getTime());
            int index = IntStream.range(0, result1.size())
                    .filter(i -> elem.equals(result1.get(i)))
                    .findFirst()
                    .orElse(-100);
            CurrencyRateInTime el = result2.get(index);
            currencyRateInTime.setRate(elem.getRate() - el.getRate() );
            result.add(currencyRateInTime);
        }

        return result;
    }


}
