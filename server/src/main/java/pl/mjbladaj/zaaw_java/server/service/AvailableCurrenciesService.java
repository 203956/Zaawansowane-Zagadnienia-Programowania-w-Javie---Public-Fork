package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.entity.Currency;

import java.util.List;

public interface AvailableCurrenciesService {
    List<Currency> getAll();
    Availability isAvailable(String symbol);
}
