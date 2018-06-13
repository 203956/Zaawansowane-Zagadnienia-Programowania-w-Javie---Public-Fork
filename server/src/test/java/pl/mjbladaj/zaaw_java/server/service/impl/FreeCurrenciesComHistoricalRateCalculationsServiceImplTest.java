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
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateCalculationsService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class FreeCurrenciesComHistoricalRateCalculationsServiceImplTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @Autowired
    private HistoricalRateCalculationsService historicalRateCalculationsService;

    @TestConfiguration
    static class FreeCurrenciesComHistoricalRateServiceImplTestContextConfiguration {
        @Bean
        public HistoricalRateCalculationsService HistoricalRateCalculationsService() {
            return new HistoricalRateCalculationsImpl();
        }
    }
    private String getValidDate() {return TimeConverter.convertDateToString(new DateTime()); }

    private String getValidFutureDate() {
        long date = System.currentTimeMillis() + 3*24*60*60*1000;
        return TimeConverter.convertDateToString(new DateTime(date) );
    }
    private String getInValidFutureDate() {
        long date = System.currentTimeMillis() -10*24*60*60*1000;
        return TimeConverter.convertDateToString(new DateTime(date) );
    }
    @Before
    public void setUp() throws Exception {
        setUpAvailableCurrenciesService();
        setUpSelectedCurrencyRateDao();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(selectedCurrencyHistoryRateDao, availableCurrenciesService);
    }
    private List<UniversalCurrencyRateInTime> getCurrencyListRateInTime() {
        List<UniversalCurrencyRateInTime> result = new ArrayList<>();
        for (int i = 0; i < 4 ; i++) {
            result.add(UniversalCurrencyRateInTime
                    .builder()
                    .rate(3.4554)
                    .time(TimeConverter.convertStringToDateTime(getValidDate()))
                    .build());
        }
        return result;
    }
    private List<UniversalCurrencyRateInTime> getCurrencyListRateInTime2() {
        List<UniversalCurrencyRateInTime> result = new ArrayList<>();
        for (int i = 0; i < 4 ; i++) {
            result.add(UniversalCurrencyRateInTime
                    .builder()
                    .rate(4.4554)
                    .time(TimeConverter.convertStringToDateTime(getValidDate()))
                    .build());
        }
        return result;
    }

    private void setUpSelectedCurrencyRateDao() throws EntityNotFoundException, TimePeriodNotAvailableException {
        Mockito.when(selectedCurrencyHistoryRateDao.getGivenPeriodRate("PLN", "EUR", getValidDate(), getValidFutureDate()))
                .thenReturn(getCurrencyListRateInTime());
        Mockito.when(selectedCurrencyHistoryRateDao.getGivenPeriodRate("USD", "EUR", getValidDate(), getValidFutureDate()))
                .thenReturn(getCurrencyListRateInTime2());

        Mockito.when(selectedCurrencyHistoryRateDao.getGivenPeriodRate("PLN", "EUR", getValidDate(), getInValidFutureDate()))
                .thenThrow(new TimePeriodNotAvailableException());
    }

    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("PLN", "EUR", "DOL", "DCL", "USD"))
        ))
                .thenReturn(Availability.builder().availability(true).build());
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("MVN", "JAV"))
        ))
                .thenReturn(Availability.builder().availability(false).build());
    }

    @Test
    public void shouldReturnValidDifferenceEqualsZeroInRatesRatesForGivenPeriod() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        //when
        List<UniversalCurrencyRateInTime> convertedRate = historicalRateCalculationsService.getDifferenceInRatesRatesForGivenPeriod("EUR", "PLN", "PLN", getValidDate(), getValidFutureDate());
        //then
        assertEquals(0, convertedRate.get(0).getRate(), 0.00001);
    }

    @Test
    public void shouldReturnValidDifferenceInRatesRatesForGivenPeriod() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        //when
        List<UniversalCurrencyRateInTime> convertedRate = historicalRateCalculationsService.getDifferenceInRatesRatesForGivenPeriod("EUR", "PLN", "USD", getValidDate(), getValidFutureDate());
        //then
        assertEquals(1, convertedRate.get(0).getRate(), 0.00001);
    }

    @Test
    public void shouldThrowTimePeriodNotAvailable() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        expectedException.expect(TimePeriodNotAvailableException.class);
        //when
        List<UniversalCurrencyRateInTime> convertedRate = historicalRateCalculationsService.getDifferenceInRatesRatesForGivenPeriod("EUR", "PLN", "USD", getValidDate(), getInValidFutureDate());
        //then
    }

    @Test
    public void shouldThrowCurrencyNotAvailableException() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        expectedException.expect(CurrencyNotAvailableException.class);
        //when
        List<UniversalCurrencyRateInTime> convertedRate = historicalRateCalculationsService.getDifferenceInRatesRatesForGivenPeriod("EUR", "PLN", "MVN", getValidDate(), getValidFutureDate());
        //then
    }
}

