package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRateInTime;

import java.util.*;

public abstract class RateInTimeConverter {

    public static UniversalCurrencyRateInTime getCurrencyRateInTime(FreeCurrenciesComRateInTime rate, String key, String keyDay) {
        UniversalCurrencyRateInTime universalCurrencyRateInTime = new UniversalCurrencyRateInTime();
        universalCurrencyRateInTime.setRate((double) rate.getResults().get(key).getVal().get(keyDay));
        return universalCurrencyRateInTime;
    }

    public static List<UniversalCurrencyRateInTime> getCurrenciesRateInTime(List<FreeCurrenciesComRateInTime> rate, String key) throws EntityNotFoundException {
        List<UniversalCurrencyRateInTime> result = new ArrayList<>();
        Collections.reverse(rate);
        for (FreeCurrenciesComRateInTime el: rate) {
            if(el.getResults().get(key) == null) {
                throw new EntityNotFoundException("Currency does not exist.");
            }

                Map<String, Number> map  =  el.getResults().get(key).getVal();
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    UniversalCurrencyRateInTime currencyRate = new UniversalCurrencyRateInTime();
                    currencyRate.setRate((Double)pair.getValue());
                    currencyRate.setTime(TimeConverter.convertStringToDateTime((String)pair.getKey()));

                    result.add(currencyRate);
                }

        }
        return result;
    }

}
