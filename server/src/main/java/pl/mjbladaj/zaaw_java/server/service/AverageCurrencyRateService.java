package pl.mjbladaj.zaaw_java.server.service;

import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dto.RateInWeek;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

@Service
public interface AverageCurrencyRateService {
    RateInWeek getAverageCurrencyRateInWeekForGivenPeriod(String baseCurrency, String goalCurrency, String startDay, String endDay) throws TimePeriodNotAvailableException, EntityNotFoundException, CurrencyNotAvailableException;
}
