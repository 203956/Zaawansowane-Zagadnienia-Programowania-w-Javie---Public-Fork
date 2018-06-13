package pl.mjbladaj.zaaw_java.server.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class FreeCurrenciesComResult {
    private String id;
    private String fr;
    private String to;
    private Map<String, Number> val= new HashMap<>();
}
