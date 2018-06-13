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
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.SameCurrenciesConvertException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class FreeCurrenciesComRateServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @MockBean
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private RateService rateService;

    @TestConfiguration
    static class FreeCurrenciesComRateServiceImplTestContextConfiguration {
        @Bean
        public RateService rateService() {
            return new RateServiceImpl();
        }
    }

    private UniversalRate getRate() {

        return UniversalRate
                .builder()
                .symbol("EUR")
                .rate(4.6522)
                .build();
    }

    private ArrayList<String> getInCurrencies(String... currencies) {
        return Stream.of(currencies).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Double> getAmountOfAnotherCurrency(Double... currenciesAmount) {
        return Stream.of(currenciesAmount).collect(Collectors.toCollection(ArrayList::new));
    }

    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("PLN", "EUR", "DOL", "DCL", "USD", "GBP"))
        ))
                .thenReturn(Availability.builder().availability(true).build());
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("MVN", "JAV"))
        ))
                .thenReturn(Availability.builder().availability(false).build());
    }

    @Before
    public void setUp() throws Exception {
        setUpAvailableCurrenciesService();
        setUpSelectedCurrencyRateDao();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(selectedCurrencyRateDao, availableCurrenciesService);
    }

    private void setUpSelectedCurrencyRateDao() throws EntityNotFoundException {
        Mockito.when(selectedCurrencyRateDao.getRate(
                argThat(new StringsMatcher("EUR", "USD", "GBP")),
                argThat(new StringsMatcher("PLN"))))
                .thenReturn(UniversalRate
                        .builder()
                        .symbol("EUR")
                        .rate(4.6522)
                        .build());
    }

    @Test
    public void shouldReturnValidCurrencyRate() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("EUR", "PLN");
        //then
        assertEquals(4.6522, convertedRate.getRate().doubleValue(), 0.0001);
    }

    @Test
    public void shouldThrowCurrencyNotAvailableWhenFirstCurrencyIsNotAvailable() throws CurrencyNotAvailableException, EntityNotFoundException {
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
    public void shouldReturnValidOtherCurrencyRate() throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException {
        //given
        //when
        CurrencyRate convertedRate = rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("USD", "EUR", "GBP"), "PLN");
        //then
        assertEquals(162.827, convertedRate.getRate().doubleValue(), 0.0001);
    }

    @Test
    public void shouldThrowCurrencyNotAvailableWhenOuTCurrencyIsNotAvailable() throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        CurrencyRate convertedRate = rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("USD", "EUR", "GBP"), "MVN");
        //then
    }

    @Test
    public void shouldThrowCurrencyNotAvailableWhenInCurrencyIsNotAvailable() throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        CurrencyRate convertedRate = rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("USD", "EUR", "MVN"), "PLN");
        //then
    }

    @Test
    public void shouldThrowCurrencyNotAvailableWhenInAndOutCurrenciesAreNotAvailable() throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException {
        //given
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        CurrencyRate convertedRate = rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("USD", "EUR", "MVN"), "JAV");
        //then
    }

    @Test
    public void shouldThrowSameCurrenciesConvertExceptionWhenOuTCurrencyIsNotAvailable() throws CurrencyNotAvailableException, EntityNotFoundException, SameCurrenciesConvertException {
        //given
        //expect
        expectedException.expect(SameCurrenciesConvertException.class);
        expectedException.expectMessage("Currencies are the same.");
        //when
        CurrencyRate convertedRate = rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("PLN", "EUR", "GBP"), "PLN");
        //then
    }
}