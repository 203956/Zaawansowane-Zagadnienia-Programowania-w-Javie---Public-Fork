package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class result {
    private String id;
    private String fr;
    private String to;
    private Map<String, Number> val= new HashMap<>();
}
