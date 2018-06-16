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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.mjbladaj.zaaw_java.server.StringsMatcher;
import pl.mjbladaj.zaaw_java.server.dto.ExchangeStatus;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountStateException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.secruity.TokenAuthenticationUtils;
import pl.mjbladaj.zaaw_java.server.service.CurrencyExchangeService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyExchangeRestController.class)
@WithMockUser
public class CurrencyExchangeRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CurrencyExchangeService currencyExchangeService;

    private String token;

    @Before
    public void setUp() throws Exception {
        token = TokenAuthenticationUtils.buildToken("login");

        Mockito.when(currencyExchangeService.exchange("login", "EUR",
                "USD", 10))
                .thenReturn(ExchangeStatus
                        .builder()
                        .fromCurrency("EUR")
                        .fromCurrencyAmount(10.0)
                        .toCurrency("USD")
                        .toCurrencyAmount(12.5)
                        .build());
        Mockito.when(currencyExchangeService.exchange("login", "EUR",
                "USD", 15))
                .thenThrow(new AccountStateException("Not enough money."));

        Mockito.when(currencyExchangeService.exchange("login", "MVC",
                "USD", 10))
                .thenThrow(new CurrencyNotAvailableException("Currency is not available."));


        Mockito.when(currencyExchangeService.exchange("login", "EUR",
                "MVC", 10))
                .thenThrow(new EntityNotFoundException("Currency does not exists."));
    }

    @After
    public void tearDown() throws Exception {
        Mockito.clearInvocations(currencyExchangeService);
    }

    @Test
    public void shouldExchangeCurrency() throws Exception {
        mvc.perform(get("/api/account/exchange/EUR/USD/10")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency", is("EUR")))
                .andExpect(jsonPath("$.fromCurrencyAmount", is(10.0)))
                .andExpect(jsonPath("$.toCurrency", is("USD")))
                .andExpect(jsonPath("$.toCurrencyAmount", is(12.5)));
    }
    @Test
    public void shouldReturn409WhenNotEnoughMoney() throws Exception {
        mvc.perform(get("/api/account/exchange/EUR/USD/15")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", is("Not enough money.")))
                .andExpect(status().is(409));
    }
    @Test
    public void shouldReturn404WhenNotCurrencyIsNotAvailable() throws Exception {
        mvc.perform(get("/api/account/exchange/MVC/USD/10")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", is("Currency is not available.")))
                .andExpect(status().is(404));
    }
    @Test
    public void shouldReturn404WhenNotCurrencyDoesNotExists() throws Exception {
        mvc.perform(get("/api/account/exchange/EUR/MVC/10")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", is("Currency does not exists.")))
                .andExpect(status().is(404));
    }
}