package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dto.AverageAndDeviations;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.ComplexStatisticsService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComplexStatisticsServiceImpl implements ComplexStatisticsService {

    @Autowired
    private HistoricalRateService historicalRateService;

    @Override
    public AverageAndDeviations getAverageAndDeviations(String baseCurrency, String goalCurrency, String startDay, String endDay) throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        List<UniversalCurrencyRateInTime> rates = historicalRateService.getConvertedRateForGivenPeriod(baseCurrency, goalCurrency, startDay, endDay);

        AverageAndDeviations result = new AverageAndDeviations();
        result.setAverage(calculateAverage(rates));
        result.setDeviations(calculateDeviations(rates, result.getAverage()));
        return result;
    }

    private double calculateAverage(List<UniversalCurrencyRateInTime> rates){
       return rates.stream().mapToDouble(UniversalCurrencyRateInTime::getRate)
               .average()
               .orElse(0);
    }

    private List<Double> calculateDeviations(List<UniversalCurrencyRateInTime> rates, double average) {
        List<Double> result = new ArrayList<>();
        rates.forEach( e -> result.add(average - e.getRate().doubleValue() ));
        return result;
    }
}
