package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.AvailableCurrencyDto;

import java.util.List;

public interface AvailableCurrenciesService {
    List<AvailableCurrencyDto> getAll();
    Availability isAvailable(String symbol);
}
