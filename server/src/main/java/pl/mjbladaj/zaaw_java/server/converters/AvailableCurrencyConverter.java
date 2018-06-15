package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.dto.AvailableCurrencyDto;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;

import java.util.ArrayList;
import java.util.List;

public class AvailableCurrencyConverter {
    public static AvailableCurrencyDto getAvaiableCurrencyDto(
            AvailableCurrency availableCurrency) {
        return AvailableCurrencyDto
                .builder()
                .id(availableCurrency.getId())
                .name(availableCurrency.getName())
                .symbol(availableCurrency.getSymbol())
                .build();
    }
    public static List<AvailableCurrencyDto> getAvaiableCurrencyDto(
            List<AvailableCurrency> availableCurrencies) {
        List<AvailableCurrencyDto> result
                = new ArrayList<>(availableCurrencies.size());

        availableCurrencies.forEach(
                item -> result.add(getAvaiableCurrencyDto(item))
        );

        return result;
    }
}
