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
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class SelectedCurrencyRateDaoImpl implements SelectedCurrencyRateDao {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Override
    public Rate getRate(String fromCurrency, String toCurrency) {
        ResponseEntity<Rate> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" + toCurrency, Rate.class);
        return response.getBody();
    }

    @Override
    public RateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date) {
        ResponseEntity<RateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" + toCurrency + "&date=" + date, RateInTime.class);
        return response.getBody();
    }

    @Override
    public List<RateInTime> getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate) throws EntityNotFoundException {
        return check(startDate, endDate, fromCurrency, toCurrency);


    }

    private List<RateInTime> check(String start, String end, String fromCurrency, String toCurrency) throws EntityNotFoundException {
        val startDate = start.split("-");
        DateTime d1 = new DateTime(Integer.parseInt(startDate[0]),
                Integer.parseInt(startDate[1]),
                Integer.parseInt(startDate[2]), 0, 0);
        val endDate = end.split("-");

        DateTime d2 = new DateTime(Integer.parseInt(endDate[0]),
                Integer.parseInt(endDate[1]),
                Integer.parseInt(endDate[2]), 0, 0);
        DateTime today = new DateTime();

        if(Math.abs(Days.daysBetween(today, d1).getDays()) >365) {
            if(Math.abs(Days.daysBetween(today, d2).getDays()) < 365) {
                do{
                    d1 = d1.plusDays(1);
                } while( Math.abs(Days.daysBetween(today, d1).getDays()) >365);
            } else {
                throw new EntityNotFoundException("Currency does not exist.");
            }
        }
        List<RateInTime> result = new ArrayList<>();
        DateTime dateHolder = d2;
        while (Days.daysBetween(d1, d2).getDays() > 8) {

            d2 =  d2.minusDays(8);
            result.add(sendRequest(fromCurrency, toCurrency, d2, dateHolder));
            dateHolder = d2.minusDays(1);
        }
        result.add( sendRequest(fromCurrency, toCurrency, d1, dateHolder));
        return result;
    }

    private RateInTime sendRequest(String fromCurrency, String toCurrency, DateTime startDate, DateTime endDate) {
        String d1 = startDate.year().get() + "-" + startDate.monthOfYear().get() + "-" + startDate.dayOfMonth().get();
        String d2 = endDate.year().get() + "-" + endDate.monthOfYear().get() + "-" + endDate.dayOfMonth().get();

        ResponseEntity<RateInTime> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" + toCurrency + "&date=" + d1 + "&endDate=" + d2, RateInTime.class);
        return response.getBody();
    }

}
