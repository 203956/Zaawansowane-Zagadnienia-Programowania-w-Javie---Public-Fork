package pl.mjbladaj.zaaw_java.server.dao;


import pl.mjbladaj.zaaw_java.server.dto.Rate;

public interface SelectedCurrencyRateDao {
    Rate getRate(String currency);
}
