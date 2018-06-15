package pl.mjbladaj.zaaw_java.server.dao.impl;


import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.converters.RateInTimeConverter;
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.converters.UniversalRateConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRate;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.ArrayList;
import java.util.List;

@Component
public class SelectedCurrencyHistoryRateDaoImpl implements SelectedCurrencyHistoryRateDao{
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Override
    public UniversalCurrencyRateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date) throws EntityNotFoundException, TimePeriodNotAvailableException {
        DateTime formattedDate = TimeConverter.convertStringToDateTime(date);
        checkPeriod(formattedDate);
        ResponseEntity<FreeCurrenciesComRateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                        toCurrency + "&date=" + date, FreeCurrenciesComRateInTime.class);
        FreeCurrenciesComRateInTime freeCurrenciesComRate = response.getBody();
        if (freeCurrenciesComRate.getQuery().isEmpty() || freeCurrenciesComRate.getResults().isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        UniversalCurrencyRateInTime rate = RateInTimeConverter.getCurrencyRateInTime(freeCurrenciesComRate, fromCurrency + "_" + toCurrency, date);
        return rate;
    }



    @Override
    public List<UniversalCurrencyRateInTime> getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate) throws TimePeriodNotAvailableException, EntityNotFoundException {
        DateTime start = TimeConverter.convertStringToDateTime(startDate);
        DateTime end = TimeConverter.convertStringToDateTime(endDate);
         List<FreeCurrenciesComRateInTime> rate = distributeRequest(start, end, fromCurrency, toCurrency);

        if (rate.isEmpty())
            throw new EntityNotFoundException("Currency does not exist.");

        return  RateInTimeConverter.getCurrenciesRateInTime(rate, fromCurrency + "_" + toCurrency);
    }

    private void checkPeriod(DateTime startDate) throws TimePeriodNotAvailableException {
        DateTime today = new DateTime();
        if(Math.abs(Days.daysBetween(today, startDate).getDays()) >
                Integer.parseInt(env.getProperty("exchange.currency.base.max.history"))) {
            throw new TimePeriodNotAvailableException();
        }
    }

    private List<FreeCurrenciesComRateInTime> distributeRequest(DateTime startDate, DateTime endDate, String baseCurrency, String goalCurrency) throws TimePeriodNotAvailableException {
        checkPeriod(startDate);
        List<FreeCurrenciesComRateInTime> result = new ArrayList<>();
        DateTime dateHolder = endDate;
        int maxDaysInOneRequest =  Integer.parseInt(env.getProperty("exchange.currency.base.max.period"));
        while( Days.daysBetween( startDate, endDate ).getDays() >= maxDaysInOneRequest ) {
            endDate =  endDate.minusDays( maxDaysInOneRequest+ 1 );
            result.add(sendRequest(baseCurrency, goalCurrency, endDate, dateHolder));
            dateHolder = endDate.minusDays(1);
        }
        result.add( sendRequest(baseCurrency, goalCurrency, startDate, dateHolder));
        return result;
    }

    private FreeCurrenciesComRateInTime sendRequest(String fromCurrency, String toCurrency, DateTime startDate, DateTime endDate) {
        ResponseEntity<FreeCurrenciesComRateInTime> response = restTemplate
                .getForEntity(getUrl(fromCurrency, toCurrency, startDate, endDate), FreeCurrenciesComRateInTime.class);
        String a = getUrl(fromCurrency, toCurrency, startDate, endDate);
        return response.getBody();
    }

    private String getUrl(String fromCurrency, String toCurrency, DateTime startDate, DateTime endDate) {
        StringBuilder stringBuilder = new StringBuilder();

        String start = TimeConverter.convertDateToString(startDate);
        String end = TimeConverter.convertDateToString(endDate);

        stringBuilder.append(env.getProperty("exchange.currency.base.url"));
        stringBuilder.append(fromCurrency);
        stringBuilder.append("_");
        stringBuilder.append(toCurrency);
        stringBuilder.append("&date=");
        stringBuilder.append(start);
        stringBuilder.append("&endDate=");
        stringBuilder.append(end);

        return stringBuilder.toString();
    }


}
