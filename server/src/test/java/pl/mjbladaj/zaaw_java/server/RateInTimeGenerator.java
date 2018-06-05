package pl.mjbladaj.zaaw_java.server;

import org.joda.time.DateTime;
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRateInTime;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComResult;

import java.util.HashMap;
import java.util.Map;

public abstract class RateInTimeGenerator {
    private static Map<String, Number> getQueryMap() {
        Map<String, Number> queryMap = new HashMap<>();
        queryMap.put("query", 1);
        return queryMap;
    }
    private static String getDate() {
        return "2018-01-17";
    }

    private static String endDate() {
        return "2018-01-20";
    }

    private static String getValidDtae() { return TimeConverter.convertDateToString(new DateTime()); }

    private static Map<String, FreeCurrenciesComResult> getResults(int amount) {
        Map<String, FreeCurrenciesComResult> resultMap =
                new HashMap<>();
        FreeCurrenciesComResult result = new FreeCurrenciesComResult();
        result.setId("PLN_USD");
        result.setFr("PLN");
        result.setTo("USD");
        Map<String, Number> vals = new HashMap<>();
        for (int i = 0; i < amount ; i++) {
            vals.put(getValidDtae(), 4.1582);
        }

        result.setVal(vals);
        resultMap.put("PLN_USD", result);
        return resultMap;
    }


    public static FreeCurrenciesComRateInTime getRateInTime(int amount) {
        return FreeCurrenciesComRateInTime
                .builder()
                .query(getQueryMap())
                .date(getDate())
                .endDate(endDate())
                .results(getResults(amount))
                .build();
    }
    public static FreeCurrenciesComRateInTime getEmptyRateInTime() {
        return FreeCurrenciesComRateInTime
                .builder()
                .query(new HashMap<>())
                .date("")
                .results(new HashMap<>())
                .build();
    }

}
