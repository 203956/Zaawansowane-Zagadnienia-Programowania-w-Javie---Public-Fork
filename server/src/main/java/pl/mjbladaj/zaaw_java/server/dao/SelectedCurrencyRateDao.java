package pl.mjbladaj.zaaw_java.server.dao;


import pl.mjbladaj.zaaw_java.server.dto.Rate;
import pl.mjbladaj.zaaw_java.server.dto.RateInTime;

public interface SelectedCurrencyRateDao {
    Rate getRate(String fromCurrency, String toCurrency);
    RateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date);
    RateInTime getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate);
}
