package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RateInTime {
    private Map<String, Number> query = new HashMap<>();
    private String date;
    private String endDate;
    private Map<String, result> results = new HashMap<>();


}
