package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;

import java.util.List;

@Service
public class HistoricalRateServiceImpl implements HistoricalRateService {
    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Override
    public UniversalCurrencyRateInTime getConvertedRateForGivenDay(String fromCurrency, String toCurrency, String date) throws TimePeriodNotAvailableException, EntityNotFoundException {

        return  selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, date);
    }

    @Override
    public List<UniversalCurrencyRateInTime> getConvertedRateForGivenPeriod(String fromCurrency, String toCurrency, String startDay, String endDay) throws TimePeriodNotAvailableException, EntityNotFoundException {
        return selectedCurrencyHistoryRateDao.getGivenPeriodRate(fromCurrency, toCurrency, startDay, endDay);

    }
}
