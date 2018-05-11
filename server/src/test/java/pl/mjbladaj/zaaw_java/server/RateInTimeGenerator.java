package pl.mjbladaj.zaaw_java.server;

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

    private static Map<String, FreeCurrenciesComResult> getResults() {
        Map<String, FreeCurrenciesComResult> resultMap =
                new HashMap<>();
        FreeCurrenciesComResult result = new FreeCurrenciesComResult();
        result.setId("PLN_USD");
        result.setFr("PLN");
        result.setTo("USD");
        Map<String, Number> vals = new HashMap<>();
        vals.put("2018-01-20", 4.1582);
        result.setVal(vals);
        resultMap.put("PLN_USD", result);
        return resultMap;
    }

    public static FreeCurrenciesComRateInTime getRateInTime() {
        return FreeCurrenciesComRateInTime
                .builder()
                .query(getQueryMap())
                .date(getDate())
                .endDate(endDate())
                .results(getResults())
                .build();
    }
    public static FreeCurrenciesComRateInTime getEmptyRate() {
        return FreeCurrenciesComRateInTime
                .builder()
                .query(new HashMap<>())
                .results(new HashMap<>())
                .build();
    }

    public static FreeCurrenciesComRateInTime getBody() {
        return getRateInTime();

    }
}
