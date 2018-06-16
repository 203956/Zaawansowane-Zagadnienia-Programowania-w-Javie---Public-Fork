package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AmountOfCurrenciesOnTheRelevantDates {
    private Double amountInStartDate;
    private Double amountInEndDate;
    private String Symbol;
}
