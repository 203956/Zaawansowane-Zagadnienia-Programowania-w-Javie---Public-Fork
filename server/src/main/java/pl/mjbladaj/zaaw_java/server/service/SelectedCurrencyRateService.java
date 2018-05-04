package pl.mjbladaj.zaaw_java.server.service;


import pl.mjbladaj.zaaw_java.server.dto.Rate;

import java.util.List;

public interface SelectedCurrencyRateService {
    Rate getRate(String currency);
}
