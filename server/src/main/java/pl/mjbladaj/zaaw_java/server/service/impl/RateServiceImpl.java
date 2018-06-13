package pl.mjbladaj.zaaw_java.server.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.RateConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.SameCurrenciesConvertException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;
import pl.mjbladaj.zaaw_java.server.utils.AvailabilityUtils;

import java.util.ArrayList;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @Override
    public CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException, CurrencyNotAvailableException {
        AvailabilityUtils.checkAvailability(availableCurrenciesService, fromCurrency, toCurrency);
        UniversalRate rate = selectedCurrencyRateDao.getRate(fromCurrency, toCurrency);
        return RateConverter.getCurrencyRate(rate);
    }

    public CurrencyRate getAmountOfAnotherCurrency(ArrayList<Double> amountOfInCurrencies, ArrayList<String> inCurrencies, String outCurrency) throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException {
        checkAvability(inCurrencies, outCurrency);
        checkIfSameCurrencies(inCurrencies, outCurrency);

        double outCurrencyRate = 0.0;
        for (int i = 0; i < inCurrencies.size(); i++) {
            outCurrencyRate += (selectedCurrencyRateDao.getRate(inCurrencies.get(i), outCurrency).getRate()).doubleValue() * amountOfInCurrencies.get(i);
        }

        UniversalRate rate = new UniversalRate();
        rate.setSymbol(outCurrency);
        rate.setRate(outCurrencyRate);
        return RateConverter.getCurrencyRate(rate);
    }

    private void checkIfSameCurrencies(ArrayList<String> inCurrencies, String outCurrency) throws SameCurrenciesConvertException {
        for(String currency : inCurrencies) {
            if(currency.equals(outCurrency)) {
                throw new SameCurrenciesConvertException("Currencies are the same.");
            }
        }
    }

    private void checkAvability(ArrayList<String> currencies, String toCurrency) throws CurrencyNotAvailableException {
        if(!availableCurrenciesService.isAvailable(toCurrency).isAvailability()) {
            throw new CurrencyNotAvailableException("Currency is not available.");
        }
        for(String currency : currencies) {
            if(!availableCurrenciesService.isAvailable(currency).isAvailability()) {
                throw new CurrencyNotAvailableException("Currency is not available.");
            }
        }
    }

    private void checkAvability(String fromCurrency, String toCurrency) throws CurrencyNotAvailableException {
        if(!availableCurrenciesService.isAvailable(toCurrency).isAvailability() ||
                !availableCurrenciesService.isAvailable(fromCurrency).isAvailability()) {
            throw new CurrencyNotAvailableException("Currency is not available.");
        }
    }

}
