package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.RateInWeek;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AverageCurrencyRateService;

import java.util.List;

@Service
public class AverageCurrencyRateServiceImpl implements AverageCurrencyRateService {

    @Autowired
    SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Override
    public RateInWeek getAverageCurrencyRateInWeekForGivenPeriod(String baseCurrency, String goalCurrency, String startDay, String endDay) throws TimePeriodNotAvailableException, EntityNotFoundException {
            List<UniversalCurrencyRateInTime> rates = selectedCurrencyHistoryRateDao.getGivenPeriodRate(baseCurrency, goalCurrency, startDay, endDay);
            RateInWeek rateInWeek = new RateInWeek();
            RateInWeek amout = new RateInWeek();

            rates.forEach((UniversalCurrencyRateInTime rate) -> {
                switch (rate.getTime().getDayOfWeek()) {
                    case 1: {
                        rateInWeek.setMonday(rateInWeek.getMonday() + rate.getRate().doubleValue());
                        amout.setMonday(amout.getMonday() + 1);
                        break;
                    }
                    case 2: {
                        rateInWeek.setTuesday(rateInWeek.getTuesday() + rate.getRate().doubleValue());
                        amout.setTuesday(amout.getTuesday() + 1);
                        break;
                    }
                    case 3: {
                        rateInWeek.setWednesday(rateInWeek.getWednesday() + rate.getRate().doubleValue());
                        amout.setWednesday(amout.getWednesday() + 1);
                    }
                    case 4: {
                        rateInWeek.setThursday(rateInWeek.getThursday() + rate.getRate().doubleValue());
                        amout.setThursday(amout.getThursday() + 1);
                        break;
                    }
                    case 5: {
                        rateInWeek.setFriday(rateInWeek.getFriday() + rate.getRate().doubleValue());
                        amout.setFriday(amout.getFriday() + 1);
                        break;
                    }
                    case 6: {
                        rateInWeek.setSaturday(rateInWeek.getSaturday() + rate.getRate().doubleValue());
                        amout.setSaturday(amout.getSaturday() + 1);
                        break;
                    }
                    case 7: {
                        rateInWeek.setSunday(rateInWeek.getSunday() + rate.getRate().doubleValue());
                        amout.setSunday(amout.getSunday() + 1);
                        break;
                    }
                }
            });

            return  getAverage(rateInWeek, amout);
    }

    private RateInWeek getAverage(RateInWeek ratesSum, RateInWeek amounts) {
        ratesSum.setMonday(ratesSum.getMonday() / amounts.getMonday());
        ratesSum.setTuesday(ratesSum.getTuesday() / amounts.getTuesday());
        ratesSum.setWednesday(ratesSum.getWednesday() / amounts.getWednesday());
        ratesSum.setThursday(ratesSum.getThursday() / amounts.getThursday());
        ratesSum.setFriday(ratesSum.getFriday() / amounts.getFriday());
        ratesSum.setSaturday(ratesSum.getSaturday() / amounts.getSaturday());
        ratesSum.setSunday(ratesSum.getSunday() / amounts.getSunday());
        return  ratesSum;

    }

}
