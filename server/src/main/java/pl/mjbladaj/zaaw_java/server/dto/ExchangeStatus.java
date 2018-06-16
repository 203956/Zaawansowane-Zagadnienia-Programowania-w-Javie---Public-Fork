package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ExchangeStatus {
    private String fromCurrency;
    private Double fromCurrencyAmount;
    private String toCurrency;
    private Double toCurrencyAmount;
}
