package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.dto.Rate;


public abstract class RateConverter {

    public static CurrencyRate getCurrencyRate(Rate rate, String key) {
         CurrencyRate currencyRate = new CurrencyRate();
         currencyRate.setRate((double) rate.getResults().get(key).get("val"));
         return currencyRate;
    }
}
