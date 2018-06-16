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
import pl.mjbladaj.zaaw_java.server.StringsMatcher;
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.AverageAndDeviations;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.ComplexStatisticsService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class ComplexStatisticsServiceImplTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private HistoricalRateService historicalRateService;

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @Autowired
    private ComplexStatisticsService complexStatisticsService;

    @TestConfiguration
    static class ComplexStatisticsServiceImplTestConfiguration {
        @Bean
        public ComplexStatisticsService complexStatisticsService() {
            return new ComplexStatisticsServiceImpl();
        }
    }

    @Before
    public void setUp() throws Exception {
        setUpAvailableCurrenciesService();
        setUpHistoricalRateService();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(availableCurrenciesService, historicalRateService);
    }

    private String getValidFutureDate(int day) {
        long date = System.currentTimeMillis() + day *24*60*60*1000;
        return TimeConverter.convertDateToString(new DateTime(date) );
    }
    
    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("PLN", "EUR", "DCL"))
        ))
                .thenReturn(Availability.builder().availability(true).build());
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("DOL", "MVN"))
        ))
                .thenReturn(Availability.builder().availability(false).build());
    }

    private void setUpHistoricalRateService() throws EntityNotFoundException, TimePeriodNotAvailableException, CurrencyNotAvailableException {
        Mockito.when(historicalRateService.getConvertedRateForGivenPeriod("PLN", "EUR", getValidFutureDate(0), getValidFutureDate(10)))
                .thenReturn(getCurrencyListRateInTime());
        Mockito.when(historicalRateService.getConvertedRateForGivenPeriod("EUR", "PLN", getValidFutureDate(0), getValidFutureDate(10)))
                .thenReturn(getCurrencyListRateInTime2());
    }

    private List<UniversalCurrencyRateInTime> getCurrencyListRateInTime() {
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

    private List<UniversalCurrencyRateInTime> getCurrencyListRateInTime2() {
        List<UniversalCurrencyRateInTime> result = new ArrayList<>();
        for (int i = 0; i < 8 ; i++) {
            result.add(UniversalCurrencyRateInTime
                    .builder()
                    .rate(4.4554 + i)
                    .time(TimeConverter.convertStringToDateTime(getValidFutureDate(i)))
                    .build());
        }
        return result;
    }

    @Test
    public void shouldReturnValidAverageAndDeviations() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        //when
        AverageAndDeviations result = complexStatisticsService.getAverageAndDeviations("PLN", "EUR", getValidFutureDate(0), getValidFutureDate(10) );
        //then
        assertEquals(4.4554, result.getAverage(), 0.00001);
        assertEquals(0, result.getDeviations().get(0), 0.0001);
    }

    @Test
    public void shouldReturnValidAverageAndDeviationsWhenDifferentDeviations() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        //when
        AverageAndDeviations result = complexStatisticsService.getAverageAndDeviations("EUR", "PLN", getValidFutureDate(0), getValidFutureDate(10) );
        //then
        assertEquals(7.9554, result.getAverage(), 0.00001);
        assertEquals(-0.5, result.getDeviations().get(4), 0.0001);
        assertEquals(-1.5, result.getDeviations().get(5), 0.0001);
        assertEquals(-2.5, result.getDeviations().get(6), 0.0001);
        assertEquals(-3.5, result.getDeviations().get(7), 0.0001);
    }

    @Test
    public void shouldThrowCurrencyNotAvailableExceptionWenFirstCurrencyIsNotAvailable() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        expectedException.expect(CurrencyNotAvailableException.class);
        //when
        AverageAndDeviations result = complexStatisticsService.getAverageAndDeviations("DOL", "PLN", getValidFutureDate(0), getValidFutureDate(10) );
        //then
    }

    @Test
    public void shouldThrowCurrencyNotAvailableExceptionWenSecondCurrencyIsNotAvailable() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        expectedException.expect(CurrencyNotAvailableException.class);
        //when
        AverageAndDeviations result = complexStatisticsService.getAverageAndDeviations("PLN", "DOL", getValidFutureDate(0), getValidFutureDate(10) );
        //then
    }

    @Test
    public void shouldThrowCurrencyNotAvailableExceptionWenBothCurrenciesAreNotAvailable() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        expectedException.expect(CurrencyNotAvailableException.class);
        //when
        AverageAndDeviations result = complexStatisticsService.getAverageAndDeviations("MVN", "DOL", getValidFutureDate(0), getValidFutureDate(10) );
        //then
    }
}

