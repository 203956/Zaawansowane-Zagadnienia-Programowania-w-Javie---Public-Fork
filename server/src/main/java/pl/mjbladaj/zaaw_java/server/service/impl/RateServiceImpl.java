package pl.mjbladaj.zaaw_java.server.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.RateConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.dto.Rate;
import pl.mjbladaj.zaaw_java.server.service.RateService;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Override
    public CurrencyRate getConvertedRate(String fromCurrency, String toCurrency) {
        Rate rate = selectedCurrencyRateDao.getRate(fromCurrency, toCurrency);
        return  RateConverter.getCurrencyRate(rate, fromCurrency + "_" + toCurrency);
    }
}
