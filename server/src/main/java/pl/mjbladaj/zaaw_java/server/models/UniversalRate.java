package pl.mjbladaj.zaaw_java.server.models;

import lombok.*;


@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UniversalRate {
    private String symbol;
    private Number rate;
}
