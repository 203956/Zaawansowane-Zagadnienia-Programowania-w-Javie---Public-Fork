package pl.mjbladaj.zaaw_java.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pl.mjbladaj.zaaw_java.server.dao.impl.Val;

@Data
public class CurrencyRate {
    @JsonProperty("return")
    private Val PHP_USD;

}


