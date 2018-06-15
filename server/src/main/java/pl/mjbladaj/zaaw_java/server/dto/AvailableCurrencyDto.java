package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AvailableCurrencyDto {
    private Integer id;
    private String symbol;
    private String name;

}
