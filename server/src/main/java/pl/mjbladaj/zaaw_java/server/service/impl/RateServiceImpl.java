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

import java.util.ArrayList;

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

    public CurrencyRate getAmountOfAnotherCurrency(ArrayList<Integer> amountOfInCurrencies, ArrayList<String> inCurrencies, String outCurrency) throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException {
        checkAvability(inCurrencies, outCurrency);
        checkIfSameCurrencies(inCurrencies, outCurrency);

        UniversalRate rate = new UniversalRate();
        rate.setSymbol(outCurrency);
        rate.setRate(0.0);
        for (int i = 0; i < inCurrencies.size(); i++) {
            rate.setRate(rate.getRate() + selectedCurrencyRateDao.getRate(inCurrencies.get(i), outCurrency).getRate() * amountOfInCurrencies.get(i));
        }
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
