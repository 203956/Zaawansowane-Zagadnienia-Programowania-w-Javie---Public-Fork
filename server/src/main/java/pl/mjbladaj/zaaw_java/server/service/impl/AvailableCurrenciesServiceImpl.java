package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.AvailableCurrencyConverter;
import pl.mjbladaj.zaaw_java.server.dao.AvailableCurrencyRepository;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.AvailableCurrencyDto;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;

import java.util.List;

@Service
public class AvailableCurrenciesServiceImpl implements AvailableCurrenciesService {

    @Autowired
    private AvailableCurrencyRepository availableCurrencyRepository;

    @Override
    public List<AvailableCurrencyDto> getAll() {
        return AvailableCurrencyConverter
        .getAvailableCurrencyDto(
                availableCurrencyRepository.findAll());
    }

    @Override
    public Availability isAvailable(String symbol) {
        boolean isAvailable = availableCurrencyRepository
                .findBySymbol(symbol)
                .isPresent();
        return Availability
                .builder()
                .availability(isAvailable)
                .build();
    }
}
