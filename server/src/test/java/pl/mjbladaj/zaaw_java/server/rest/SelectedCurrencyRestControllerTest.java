package pl.mjbladaj.zaaw_java.server.rest;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.SameCurrenciesConvertException;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(SelectedCurrencyRestController.class)
public class SelectedCurrencyRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RateService rateService;

    private CurrencyRate getCurrencyRate() {
        return CurrencyRate
                .builder()
                .rate(3.4554)
                .build();
    }

    private CurrencyRate getOtherCurrencyRate() {
        return CurrencyRate
                .builder()
                .rate(155.2752)
                .build();
    }

    private ArrayList<String> getInCurrencies(String... currencies) {
        if(currencies.length > 0)
            return Stream.of(currencies).collect(Collectors.toCollection(ArrayList::new));
        else
            return null;
    }

    private ArrayList<Double> getAmountOfAnotherCurrency(Double... currenciesAmount) {
        if(currenciesAmount.length > 0)
            return Stream.of(currenciesAmount).collect(Collectors.toCollection(ArrayList::new));
        else
            return null;
    }

    @Before
    public void setUp() throws EntityNotFoundException, CurrencyNotAvailableException, SameCurrenciesConvertException {
        Mockito.when(rateService.getConvertedRate("EUR", "PLN"))
                .thenReturn(getCurrencyRate());

        Mockito.when(rateService.getConvertedRate("DOL", "PLN"))
                .thenThrow(new CurrencyNotAvailableException());

        Mockito.when(rateService.getConvertedRate("GBP", "PLN"))
                .thenThrow(new EntityNotFoundException());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("USD", "EUR", "GBP"), "PLN"))
                .thenReturn(getOtherCurrencyRate());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("DOL", "EUR", "GBP"), "PLN"))
                .thenThrow(new CurrencyNotAvailableException());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("MVN", "EUR", "GBP"), "PLN"))
                .thenThrow(new EntityNotFoundException());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0, 20.0),
                getInCurrencies("PLN", "EUR", "GBP"), "PLN"))
                .thenThrow(new SameCurrenciesConvertException());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0),
                getInCurrencies("USD", "EUR", "GBP"), "PLN"))
                .thenThrow(new IndexOutOfBoundsException());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(),
                getInCurrencies("PLN", "EUR", "GBP"), "PLN"))
                .thenThrow(new NullPointerException());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(),
                getInCurrencies(), "PLN"))
                .thenThrow(new NullPointerException());

        Mockito.when(rateService.getAmountOfAnotherCurrency(
                getAmountOfAnotherCurrency(10.0, 5.0),
                getInCurrencies(), "PLN"))
                .thenThrow(new NullPointerException());
    }

    @After
    public void tearDown() {
        Mockito.reset(rateService);
    }

    @Test
    public void shouldReturnCurrencyRate() throws Exception {
        mvc.perform(get("/api/currencies/EUR/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate", is(3.4554)));
    }

    @Test
    public void shouldReturn404WhenApiDontProvidesCurrency() throws Exception {
        mvc.perform(get("/api/currencies/GBP/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenCurrencyIsNotAvailable() throws Exception {
        mvc.perform(get("/api/currencies/DOL/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturnOtherCurrencyRate() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN&currencies=USD&currencies=EUR&currencies=GBP&amount=10&amount=5&amount=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate", is(155.2752)));
    }

    @Test
    public void shouldReturn404WhenInCurrencyIsNotAvailable() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN&currencies=DOL&currencies=EUR&currencies=GBP&amount=10&amount=5&amount=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenApiDontProvidesCurrencyf() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN&currencies=MVN&currencies=EUR&currencies=GBP&amount=10&amount=5&amount=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenCurrenciesAreTheSameIsNotAvailable() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN&currencies=PLN&currencies=EUR&currencies=GBP&amount=10&amount=5&amount=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenNotAllCurrenciesAmountNotSpecified() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN&currencies=USD&currencies=EUR&currencies=GBP&amount=10&amount=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenInCurrenciesNotSpecified() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN&amount=10&amount=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenCurrenciesAmountNotSpecified() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN&currencies=PLN&currencies=EUR&currencies=GBP")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenCurrenciesAndAmountNotSpecified() throws Exception {
        mvc.perform(get("/api/currencies/?out=PLN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
