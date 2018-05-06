package pl.mjbladaj.zaaw_java.server.dao.impl;


import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.dto.Rate;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import org.springframework.core.env.Environment;
import pl.mjbladaj.zaaw_java.server.dto.RateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;

import java.util.ArrayList;
import java.util.List;


@Service
public class SelectedCurrencyRateDaoImpl implements SelectedCurrencyRateDao {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    private int maxDaysInOneRequest = 7;

    @Override
    public Rate getRate(String fromCurrency, String toCurrency) {
        ResponseEntity<Rate> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" + toCurrency, Rate.class);
        return response.getBody();
    }

    @Override
    public RateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date) {
        ResponseEntity<RateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                        toCurrency + "&date=" + date, RateInTime.class);
        return response.getBody();
    }

    @Override
    public List<RateInTime> getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate) throws TimePeriodNotAvailableException {
        return check(startDate, endDate, fromCurrency, toCurrency);
    }

    private List<RateInTime> check(String start, String end, String baseCurrency, String goalCurrency) throws TimePeriodNotAvailableException {

        DateTime startDay = convertStringToDateTime(start);
        DateTime endDay = convertStringToDateTime(end);
        DateTime today = new DateTime();

        if(Math.abs(Days.daysBetween(today, startDay).getDays()) >365) {
            if(Math.abs(Days.daysBetween(today, endDay).getDays()) < 365) {
                do{
                    startDay = startDay.plusDays(1);
                } while( Math.abs(Days.daysBetween(today, startDay).getDays()) >365);
            } else {
                throw new TimePeriodNotAvailableException();
            }
        }
        List<RateInTime> result = new ArrayList<>();
        DateTime dateHolder = endDay;
        while( Days.daysBetween( startDay, endDay ).getDays() >= maxDaysInOneRequest ) {

            endDay =  endDay.minusDays( maxDaysInOneRequest+ 1 );
            result.add(sendRequest(baseCurrency, goalCurrency, endDay, dateHolder));
            dateHolder = endDay.minusDays(1);
        }
        result.add( sendRequest(baseCurrency, goalCurrency, startDay, dateHolder));
        return result;
    }

    private RateInTime sendRequest(String fromCurrency, String toCurrency, DateTime startDate, DateTime endDate) {
        String d1 = convertDateToString(startDate);
        String d2 = convertDateToString(endDate);

        ResponseEntity<RateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                                toCurrency + "&date=" + d1 + "&endDate=" + d2, RateInTime.class);
        return response.getBody();
    }

    private DateTime convertStringToDateTime(String stringDate) {
        val splitedDate = stringDate.split("-");
        DateTime date = new DateTime(Integer.parseInt(splitedDate[0]),
                Integer.parseInt(splitedDate[1]),
                Integer.parseInt(splitedDate[2]), 0, 0);
        return date;
    }

    private String convertDateToString(DateTime date) {
        return date.year().get() + "-" + date.monthOfYear().get() + "-" + date.dayOfMonth().get();
    }

}
