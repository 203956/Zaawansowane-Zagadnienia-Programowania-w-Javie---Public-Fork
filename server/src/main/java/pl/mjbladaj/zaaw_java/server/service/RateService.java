package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;

public interface RateService {
    CurrencyRate getConvertedRate(String fromCurrency, String toCurrency);
}
