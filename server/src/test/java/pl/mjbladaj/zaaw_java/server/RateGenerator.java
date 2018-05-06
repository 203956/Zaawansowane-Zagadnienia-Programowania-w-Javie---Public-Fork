package pl.mjbladaj.zaaw_java.server;

import pl.mjbladaj.zaaw_java.server.dao.impl.models.Rate;

import java.util.HashMap;
import java.util.Map;

public abstract class RateGenerator {
    private static Map<String, Number> getQueryMap() {
        Map<String, Number> queryMap = new HashMap<>();
        queryMap.put("query", 1);
        return queryMap;
    }
    private static Map<String, HashMap<String, Object>> getResultsMap() {
        Map<String, HashMap<String, Object>> resultMap =
                new HashMap<>();
        HashMap<String, Object> result = new HashMap<>();
        result.put("val", 4.6522);
        resultMap.put("EUR_PLN", result);
        return resultMap;
    }

    public static Rate getRate() {
        return Rate
                .builder()
                .query(getQueryMap())
                .results(getResultsMap())
                .build();
    }
    public static Rate getEmptyRate() {
        return Rate
                .builder()
                .query(new HashMap<>())
                .results(new HashMap<>())
                .build();
    }
}
