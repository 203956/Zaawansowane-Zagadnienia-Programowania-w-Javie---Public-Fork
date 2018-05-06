package pl.mjbladaj.zaaw_java.server.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Rate {

    @Builder.Default
    private Map<String, Number> query;

    @Builder.Default
    private Map<String, HashMap<String, Object>> results;
}