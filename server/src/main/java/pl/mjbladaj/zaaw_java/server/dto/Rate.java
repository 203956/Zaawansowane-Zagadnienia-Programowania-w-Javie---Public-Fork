package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Rate {
    private Map<String, Number> query = new HashMap<>();
    private Map<String, HashMap<String, Object>> results = new HashMap<>();
}