package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.RateInWeek;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.AverageCurrencyRateService;
import pl.mjbladaj.zaaw_java.server.utils.AvailabilityUtils;

import java.util.List;

@Service
public class AverageCurrencyRateServiceImpl implements AverageCurrencyRateService {

    @Autowired
    AvailableCurrenciesService availableCurrenciesService;

    @Autowired
    SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Override
    public RateInWeek getAverageCurrencyRateInWeekForGivenPeriod(String baseCurrency, String goalCurrency, String startDay, String endDay) throws TimePeriodNotAvailableException, EntityNotFoundException, CurrencyNotAvailableException {
       AvailabilityUtils.checkAvailability(availableCurrenciesService, baseCurrency, goalCurrency);

       List<UniversalCurrencyRateInTime> rates = selectedCurrencyHistoryRateDao.getGivenPeriodRate(baseCurrency, goalCurrency, startDay, endDay);
       RateInWeek rateInWeek = new RateInWeek();

       rateInWeek.setMonday(calculateAverageForGivenDayOfWeek(1, rates));
       rateInWeek.setTuesday(calculateAverageForGivenDayOfWeek(2, rates));
       rateInWeek.setWednesday(calculateAverageForGivenDayOfWeek(3, rates));
       rateInWeek.setThursday(calculateAverageForGivenDayOfWeek(4, rates));
       rateInWeek.setFriday(calculateAverageForGivenDayOfWeek(5, rates));
       rateInWeek.setSaturday(calculateAverageForGivenDayOfWeek(6, rates));
       rateInWeek.setSunday(calculateAverageForGivenDayOfWeek(7, rates));

       return  rateInWeek;
    }

    private double calculateAverageForGivenDayOfWeek(int dayOfWeek, List<UniversalCurrencyRateInTime> rates ) {
        return rates.stream().filter(e -> e.getTime().getDayOfWeek() == dayOfWeek)
                .mapToDouble(UniversalCurrencyRateInTime::getRate)
                .average()
                .orElse(0);
    }
}
