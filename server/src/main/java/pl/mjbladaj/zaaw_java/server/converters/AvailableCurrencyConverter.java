package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.dto.AvailableCurrencyDto;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AvailableCurrencyConverter {
    public static AvailableCurrencyDto getAvailableCurrencyDto(
            AvailableCurrency availableCurrency) {
        return AvailableCurrencyDto
                .builder()
                .id(availableCurrency.getId())
                .name(availableCurrency.getName())
                .symbol(availableCurrency.getSymbol())
                .build();
    }
    public static List<AvailableCurrencyDto> getAvailableCurrencyDto(
            List<AvailableCurrency> availableCurrencies) {
        return availableCurrencies
                .stream()
                .map(item -> getAvailableCurrencyDto(item))
                .collect(Collectors.toList());
    }
}
