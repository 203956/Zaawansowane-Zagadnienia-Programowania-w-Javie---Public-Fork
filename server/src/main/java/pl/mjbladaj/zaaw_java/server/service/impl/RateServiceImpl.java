package pl.mjbladaj.zaaw_java.server.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.RateConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;
import pl.mjbladaj.zaaw_java.server.utils.AvailabilityUtils;

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
}
