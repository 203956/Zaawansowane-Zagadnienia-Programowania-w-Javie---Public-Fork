package pl.mjbladaj.zaaw_java.server.service;
import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

public interface BasicPriceCurrencyIndexService {
    Index getBasicPriceCurrencyForGivenCurrencyInPeriod(String currencySymbol, String startDate, String endDate ) throws TimePeriodNotAvailableException, EntityNotFoundException;
}
