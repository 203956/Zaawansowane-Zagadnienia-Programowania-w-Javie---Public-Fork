package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class Rate {
    private Boolean success;
    private String terms;
    private String privacy;
    private String source;
    private Long timestamp;
    private HashMap<String, Number> quotes = new HashMap<>();;
}
