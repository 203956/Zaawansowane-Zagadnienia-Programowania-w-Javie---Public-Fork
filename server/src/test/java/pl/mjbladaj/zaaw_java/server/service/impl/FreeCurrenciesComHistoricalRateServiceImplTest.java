package pl.mjbladaj.zaaw_java.server.service.impl;

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
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRateInTime;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComResult;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class FreeCurrenciesComHistoricalRateServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @Autowired
    private HistoricalRateService historicalRateService;

    @TestConfiguration
    static class FreeCurrenciesComHistoricalRateServiceImplTestContextConfiguration {
        @Bean
        public HistoricalRateService historicalRateService() {
            return new HistoricalRateServiceImpl();
        }
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

    private void setUpSelectedCurrencyRateDao() throws EntityNotFoundException, TimePeriodNotAvailableException {
        Mockito.when(selectedCurrencyHistoryRateDao.getGivenDayRate("EUR", "PLN", "2018-04-22"))
                .thenReturn(UniversalCurrencyRateInTime
                        .builder()
                        .time(TimeConverter.convertStringToDateTime("2018-04-22"))
                        .rate(4.6522)
                        .build());
    }
    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("PLN", "EUR", "DOL", "DCL"))
        ))
                .thenReturn(Availability.builder().availability(true).build());
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("MVN", "JAV"))
        ))
                .thenReturn(Availability.builder().availability(false).build());
    }

    @Test
    public void shouldReturnValidHistoricalCurrencyRate() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {
        //given
        //when
        UniversalCurrencyRateInTime convertedRate = historicalRateService.getConvertedRateForGivenDay("EUR", "PLN", "2018-04-22");
        //then
        assertEquals(4.6522, convertedRate.getRate(), 0.00001);
    }






}
