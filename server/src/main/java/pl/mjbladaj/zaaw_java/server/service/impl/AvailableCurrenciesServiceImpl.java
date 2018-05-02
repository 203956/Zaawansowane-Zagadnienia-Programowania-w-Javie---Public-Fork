package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.CurrencyRepository;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.entity.Currency;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;

import java.util.List;

@Service
public class AvailableCurrenciesServiceImpl implements AvailableCurrenciesService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    @Override
    public Availability isAvailable(String symbol) {
        boolean isAvailable = currencyRepository
                .findBySymbol(symbol)
                .isPresent();
        return Availability
                .builder()
                .availability(isAvailable)
                .build();
    }
}
