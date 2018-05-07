package pl.mjbladaj.zaaw_java.server.dao.impl;


import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.RateInTime;
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
    public RateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date) {
        System.out.println(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                toCurrency + "&date=" + date);
        ResponseEntity<RateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                        toCurrency + "&date=" + date, RateInTime.class);
        return response.getBody();
    }

    @Override
    public List<RateInTime> getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate) throws TimePeriodNotAvailableException {
        DateTime start = TimeConverter.convertStringToDateTime(startDate);
        DateTime end = TimeConverter.convertStringToDateTime(endDate);
        return distributeRequest(start, end, fromCurrency, toCurrency);
    }
    private void checkPeriod(DateTime startDate) throws TimePeriodNotAvailableException {
        DateTime today = new DateTime();
        if(Math.abs(Days.daysBetween(today, startDate).getDays()) >
                Integer.parseInt(env.getProperty("exchange.currency.base.max.history"))) {
            System.out.printf(Days.daysBetween(today, startDate).getDays() + "a");
            throw new TimePeriodNotAvailableException();
        }
    }

    private List<RateInTime> distributeRequest(DateTime startDate, DateTime endDate, String baseCurrency, String goalCurrency) throws TimePeriodNotAvailableException {
        checkPeriod(startDate);
        List<RateInTime> result = new ArrayList<>();
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


//TDODO zmienic metode do tworzenia url
    private RateInTime sendRequest(String fromCurrency, String toCurrency, DateTime startDate, DateTime endDate) {
        String start = TimeConverter.convertDateToString(startDate);
        String end = TimeConverter.convertDateToString(endDate);

        System.out.println( env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                toCurrency + "&date=" + start + "&endDate=" + end);
        ResponseEntity<RateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                        toCurrency + "&date=" + start + "&endDate=" + end, RateInTime.class);
        return response.getBody();
    }


}
