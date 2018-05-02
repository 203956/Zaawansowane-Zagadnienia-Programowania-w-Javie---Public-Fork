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
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.entity.Currency;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(AvailableCurrenciesRest.class)
public class AvailableCurrenciesRestTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;
    private List<Currency> getAvailableCurrencies() {
        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(Currency
                .builder()
                .id(1)
                .symbol("AUD")
                .build());

        currencies.add(Currency
                .builder()
                .id(2)
                .symbol("CHF")
                .build());
        return currencies;
    }
    @Before
    public void setUp() {
        Mockito.when(availableCurrenciesService.getAll())
                .thenReturn(getAvailableCurrencies());

        Mockito.when(availableCurrenciesService.isAvailable("PLN"))
                .thenReturn(Availability.builder().availability(true).build());

        Mockito.when(availableCurrenciesService.isAvailable("MVN"))
                .thenReturn(Availability.builder().availability(false).build());
    }

    @After
    public void tearDown() {
        Mockito.reset(availableCurrenciesService);
    }
    @Test
    public void shouldReturnListOfAvailableCurrencies() throws Exception {
        mvc.perform(get("/api/currencies/available")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].symbol", is("AUD")))
                .andExpect(jsonPath("$[1].symbol", is("CHF")));
    }
    @Test
    public void shouldReturnTrueWhenAskForAvailableCurrency() throws Exception {
        mvc.perform(get("/api/currencies/available/PLN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availability", is(true)));
    }
    @Test
    public void shouldReturnFalseWhenAskForNonAvailableCurrency() throws Exception {
        mvc.perform(get("/api/currencies/available/MVN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availability", is(false)));
    }
}