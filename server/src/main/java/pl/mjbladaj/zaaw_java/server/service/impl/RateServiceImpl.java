package pl.mjbladaj.zaaw_java.server.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.RateConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.models.Rate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @Override
    public CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) throws EntityNotFoundException, CurrencyNotAvailableException {
        checkAvability(fromCurrency, toCurrency);
        Rate rate = selectedCurrencyRateDao.getRate(fromCurrency, toCurrency);
        checkReuestsResults(rate);
        return  RateConverter.getCurrencyRate(rate, fromCurrency + "_" + toCurrency);
    }

    private void checkAvability(String fromCurrency, String toCurrency) throws CurrencyNotAvailableException {
        if(!availableCurrenciesService.isAvailable(toCurrency).isAvailability() ||
                !availableCurrenciesService.isAvailable(fromCurrency).isAvailability()) {
            throw new CurrencyNotAvailableException("Currency is not available.");
        }
    }

    private void checkReuestsResults(Rate rate) throws EntityNotFoundException {
        if (rate.getQuery().isEmpty() || rate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exists.");
    }
}
