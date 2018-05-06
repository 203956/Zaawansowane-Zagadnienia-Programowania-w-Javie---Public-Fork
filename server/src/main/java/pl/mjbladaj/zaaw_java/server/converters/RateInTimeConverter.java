package pl.mjbladaj.zaaw_java.server.converters;

import lombok.experimental.var;
import lombok.val;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.dto.RateInTime;

import java.util.*;

public abstract class RateInTimeConverter {

    public static CurrencyRateInTime getCurrencyRateInTime(RateInTime rate, String key,String keyDay) {
        CurrencyRateInTime currencyRateInTime = new CurrencyRateInTime();
        currencyRateInTime.setRate((double) rate.getResults().get(key).getVal().get(keyDay));
        return currencyRateInTime;
    }

    public static List<CurrencyRateInTime> getCurrenciesRateInTime(RateInTime rate, String key) {
        List<CurrencyRateInTime> result = new ArrayList<>();
        val map  =  rate.getResults().get(key).getVal();
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            CurrencyRateInTime currencyRateInTime = new CurrencyRateInTime();
            currencyRateInTime.setRate((Double)pair.getValue());
            currencyRateInTime.setTime((String)pair.getKey());
            it.remove(); // avoids a ConcurrentModificationException

            result.add(currencyRateInTime);
        }
        return result;
    }

    public static List<CurrencyRateInTime> getCurrenciesRateInTime(List<RateInTime> rate, String key) {
        List<CurrencyRateInTime> result = new ArrayList<>();
        Collections.reverse(rate);
        for (val el: rate) {
            val map  =  el.getResults().get(key).getVal();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                CurrencyRateInTime currencyRate = new CurrencyRateInTime();
                currencyRate.setRate((Double)pair.getValue());
                currencyRate.setTime((String)pair.getKey());

                result.add(currencyRate);
                it.remove();
            }
        }

        return result;
    }

}
