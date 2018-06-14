package pl.mjbladaj.zaaw_java.server.dao;


import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;

public interface SelectedCurrencyRateDao {
    UniversalRate getRate(String fromCurrency, String toCurrency) throws EntityNotFoundException;
}

