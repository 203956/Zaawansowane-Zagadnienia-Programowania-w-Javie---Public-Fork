package pl.mjbladaj.zaaw_java.server.service.impl;


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
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.List;

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
    public CurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws EntityNotFoundException {
        RateInTime rate = selectedCurrencyRateDao.getGivenDayRate(fromCurrency, toCurrency, date);

        if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        return  RateInTimeConverter.getCurrencyRateInTime(rate, fromCurrency + "_" + toCurrency, date);
    }

    @Override
    public List<CurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws EntityNotFoundException {
        RateInTime rate = selectedCurrencyRateDao.getGivenPeriodRate(fromCurrency, toCurrency, startDay, endDay);

        if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        return  RateInTimeConverter.getCurrenciesRateInTime(rate, fromCurrency + "_" + toCurrency);

    }


}
