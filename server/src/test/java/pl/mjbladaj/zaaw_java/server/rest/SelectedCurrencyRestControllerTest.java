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
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.service.RateService;
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
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setRate(3.4554);
        return currencyRate;
    }

    @Before
    public void setUp() throws EntityNotFoundException {
        Mockito.when(rateService.getConvertedRate("EUR", "PLN"))
                .thenReturn(getCurrencyRate());
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
/*
    @Test
    public void shouldThrowEntityNotFoundExceptionWhenAskForNonAvailableCurrency() throws Exception {
        mvc.perform(get("/api/currencies/DOL/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
    */
}
