package pl.mjbladaj.zaaw_java.server.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FreeCurrenciesComRateInTime {
    private Map<String, Number> query = new HashMap<>();
    private String date;
    private String endDate;
    private Map<String, FreeCurrenciesComResult> results = new HashMap<>();
}
