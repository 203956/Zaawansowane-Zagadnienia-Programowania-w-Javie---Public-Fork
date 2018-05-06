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
        DateTime start = convertStringToDateTime(startDate);
        DateTime end = convertStringToDateTime(endDate);
        return distributeRquest(start, end, fromCurrency, toCurrency);
    }

    private List<RateInTime> distributeRquest(DateTime startDate, DateTime endDate, String baseCurrency, String goalCurrency) throws TimePeriodNotAvailableException {
        DateTime today = new DateTime();
        if(Math.abs(Days.daysBetween(today, startDate).getDays()) >365) {
            if(Math.abs(Days.daysBetween(today, endDate).getDays()) < 365) {
                do{
                    startDate = startDate.plusDays(1);
                } while( Math.abs(Days.daysBetween(today, startDate).getDays()) >365);
            } else {
                throw new TimePeriodNotAvailableException();
            }
        }
        List<RateInTime> result = new ArrayList<>();
        DateTime dateHolder = endDate;
        while( Days.daysBetween( startDate, endDate ).getDays() >= maxDaysInOneRequest ) {
            endDate =  endDate.minusDays( maxDaysInOneRequest+ 1 );
            result.add(sendRequest(baseCurrency, goalCurrency, endDate, dateHolder));
            dateHolder = endDate.minusDays(1);
        }
        result.add( sendRequest(baseCurrency, goalCurrency, startDate, dateHolder));
        return result;
    }

    private RateInTime sendRequest(String fromCurrency, String toCurrency, DateTime startDate, DateTime endDate) {
        String start = convertDateToString(startDate);
        String end = convertDateToString(endDate);

        ResponseEntity<RateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" +
                                toCurrency + "&date=" + start + "&endDate=" + end, RateInTime.class);
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
