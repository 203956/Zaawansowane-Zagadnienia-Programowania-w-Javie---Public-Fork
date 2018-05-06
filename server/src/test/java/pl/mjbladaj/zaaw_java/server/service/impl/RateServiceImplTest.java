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
import pl.mjbladaj.zaaw_java.server.dao.AvailableCurrencyRepository;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dao.impl.SelectedCurrencyRateDaoImpl;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.Rate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class RateServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @MockBean
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private RateService rateService;

    @TestConfiguration
    static class RateServiceImplTestContextConfiguration {
        @Bean
        public RateService rateService() {
            return new RateServiceImpl();
        }

    }

    private Map<String, Number> getQueryMap() {
        Map<String, Number> queryMap = new HashMap<>();
        queryMap.put("query", 1);
        return queryMap;
    }
    private Map<String, HashMap<String, Object>> getResultsMap() {
        Map<String, HashMap<String, Object>> resultMap =
                new HashMap<>();
        HashMap<String, Object> result = new HashMap<>();
        result.put("val", 4.6522);
        resultMap.put("EUR_PLN", result);
        return resultMap;
    }
    private Rate getRate() {
        return Rate
                .builder()
                .query(getQueryMap())
                .results(getResultsMap())
                .build();
    }
    private Rate getEmptyRate() {
        return Rate
                .builder()
                .build();
    }

    private void setUpSelectedCurrencyRateDao() {
        Mockito.when(selectedCurrencyRateDao.getRate("EUR", "PLN"))
                .thenReturn(getRate());

        Mockito.when(selectedCurrencyRateDao.getRate("DOL", "PLN"))
                .thenReturn(getEmptyRate());

        Mockito.when(selectedCurrencyRateDao.getRate("EUR", "DOL"))
                .thenReturn(getEmptyRate());

        Mockito.when(selectedCurrencyRateDao.getRate("DOL", "DCL"))
                .thenReturn(getEmptyRate());
    }

    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable("PLN"))
                .thenReturn(Availability.builder().availability(true).build());

        Mockito.when(availableCurrenciesService.isAvailable("EUR"))
                .thenReturn(Availability.builder().availability(true).build());

        Mockito.when(availableCurrenciesService.isAvailable("DOL"))
                .thenReturn(Availability.builder().availability(true).build());

        Mockito.when(availableCurrenciesService.isAvailable("DCL"))
                .thenReturn(Availability.builder().availability(true).build());

        Mockito.when(availableCurrenciesService.isAvailable("MVN"))
                .thenReturn(Availability.builder().availability(false).build());

        Mockito.when(availableCurrenciesService.isAvailable("JAV"))
                .thenReturn(Availability.builder().availability(false).build());
    }

    @Before
    public void setUp() throws Exception {
        setUpAvailableCurrenciesService();
        setUpSelectedCurrencyRateDao();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(selectedCurrencyRateDao);
    }

    @Test
    public void shouldReturnValidCurrencyRate() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("EUR", "PLN");
        //then
        assertEquals(4.6522, convertedRate.getRate(), 0.00001);
    }

    @Test
    public void shouldThrowCurrencyNotAvailableWhenFirstCurrencyIsNotAbvailable() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("MVN", "PLN");
        //then
    }
    @Test
    public void shouldThrowCurrencyNotAvailableWhenSecondCurrencyIsNotAbvailable() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("EUR", "MVN");
        //then
    }

    @Test
    public void shouldThrowCurrencyNotAvailableWhenBothCurrenciesIsNotAvailable() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("MVN", "JAV");
        //then
    }

    @Test
    public void shouldThrowEntityNotFoundWhenFirstCurrencyIsNotProvidedByApi() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("DOL", "PLN");
        //then
    }

    @Test
    public void shouldThrowEntityNotFoundWhenSecondCurrencyIsNotProvidedByApi() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("PLN", "DOL");
        //then
    }

    @Test
    public void shouldThrowEntityNotFoundWhenBothCurrencyIsNotProvidedByApi() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("DOL", "DCL");
        //then
    }
}