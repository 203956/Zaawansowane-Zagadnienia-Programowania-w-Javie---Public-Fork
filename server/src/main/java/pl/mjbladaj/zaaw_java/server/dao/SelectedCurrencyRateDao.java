package pl.mjbladaj.zaaw_java.server.dao;


import pl.mjbladaj.zaaw_java.server.models.Rate;

public interface SelectedCurrencyRateDao {
    Rate getRate(String fromCurrency, String toCurrency);
}
