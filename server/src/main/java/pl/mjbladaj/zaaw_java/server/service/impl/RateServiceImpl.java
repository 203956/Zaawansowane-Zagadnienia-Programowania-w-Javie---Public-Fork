package pl.mjbladaj.zaaw_java.server.service.impl;


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
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

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
}
