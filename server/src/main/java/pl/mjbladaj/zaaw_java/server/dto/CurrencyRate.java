package pl.mjbladaj.zaaw_java.server.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.mjbladaj.zaaw_java.server.dao.impl.Val;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CurrencyRate {
    private Double rate;
}

@Data
public class CurrencyRate {
    @JsonProperty("return")
    private Val PHP_USD;

}
