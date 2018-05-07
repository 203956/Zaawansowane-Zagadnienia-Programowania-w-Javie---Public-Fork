package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;


public abstract class RateConverter {

    public static CurrencyRate getCurrencyRate(UniversalRate rate) {
         return
                 CurrencyRate
                         .builder()
                         .rate(rate.getRate())
                         .build();
    }
}
