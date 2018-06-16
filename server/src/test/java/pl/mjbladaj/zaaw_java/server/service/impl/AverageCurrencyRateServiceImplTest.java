package pl.mjbladaj.zaaw_java.server.service.impl;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.RateInWeek;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AverageCurrencyRateService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class AverageCurrencyRateServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @Autowired
    AverageCurrencyRateService averageCurrencyRateService;

    @TestConfiguration
    static class AverageCurrencyRateServiceImplTestContextConfiguration {
        @Bean
        public AverageCurrencyRateService AverageCurrencyRateService() {
            return new AverageCurrencyRateServiceImpl();
        }
    }

    @Before
    public void setUp() throws Exception {
        setUpSelectedCurrencyRateDao();
    }

    @After
    public void tearDown() {
        Mockito.reset(selectedCurrencyHistoryRateDao);
    }
    private String getValidDate() {return TimeConverter.convertDateToString(new DateTime()); }

    private String getValidFutureDate() {
        long date = System.currentTimeMillis() + 8*24*60*60*1000;
        return TimeConverter.convertDateToString(new DateTime(date) );
    }

    private String getValidFutureDate(int day) {
        long date = System.currentTimeMillis() + day *24*60*60*1000;
        return TimeConverter.convertDateToString(new DateTime(date) );
    }

    private String getInValidFutureDate() {
        long date = System.currentTimeMillis() -10*24*60*60*1000;
        return TimeConverter.convertDateToString(new DateTime(date) );
    }

    private void setUpSelectedCurrencyRateDao() throws EntityNotFoundException, TimePeriodNotAvailableException {
        Mockito.when(selectedCurrencyHistoryRateDao.getGivenPeriodRate("EUR", "PLN", getValidDate(), getValidFutureDate()))
                .thenReturn(getCurrencyListRateInTime2());
        Mockito.when(selectedCurrencyHistoryRateDao.getGivenPeriodRate("EUR", "PLN", getValidDate(), getInValidFutureDate()))
                .thenThrow(new EntityNotFoundException());
    }

    private List<UniversalCurrencyRateInTime> getCurrencyListRateInTime2() {
        List<UniversalCurrencyRateInTime> result = new ArrayList<>();
        for (int i = 0; i < 8 ; i++) {
            result.add(UniversalCurrencyRateInTime
                    .builder()
                    .rate(4.4554)
                    .time(TimeConverter.convertStringToDateTime(getValidFutureDate(i)))
                    .build());
        }
        return result;
    }

    @Test
    public void shouldReturnValidAverageRateForEveryDayOfWeek() throws EntityNotFoundException, TimePeriodNotAvailableException, CurrencyNotAvailableException {
        //given
        //when
        RateInWeek rate = averageCurrencyRateService.getAverageCurrencyRateInWeekForGivenPeriod("EUR", "PLN", getValidDate(), getValidFutureDate() );
        //then
        assertEquals(4.4554, rate.getMonday(), 0.00001);
        assertEquals(4.4554, rate.getTuesday(), 0.00001);
        assertEquals(4.4554, rate.getWednesday(), 0.00001);
        assertEquals(4.4554, rate.getThursday(), 0.00001);
        assertEquals(4.4554, rate.getFriday(), 0.00001);
        assertEquals(4.4554, rate.getSaturday(), 0.00001);
        assertEquals(4.4554, rate.getSunday(), 0.00001);
    }

    @Test
    public void shouldThrowEntityNotFound() throws EntityNotFoundException, TimePeriodNotAvailableException, CurrencyNotAvailableException {
        //given
        //when
        expectedException.expect(EntityNotFoundException.class);
        RateInWeek rate = averageCurrencyRateService.getAverageCurrencyRateInWeekForGivenPeriod("EUR", "PLN", getValidDate(), getInValidFutureDate() );
        //then
    }
}
