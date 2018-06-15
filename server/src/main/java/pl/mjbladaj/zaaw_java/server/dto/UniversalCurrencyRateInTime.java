package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;
import org.joda.time.DateTime;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UniversalCurrencyRateInTime {
    private Double rate;
    private DateTime time;

public Double getRate() {
    return this.rate;
}
}
