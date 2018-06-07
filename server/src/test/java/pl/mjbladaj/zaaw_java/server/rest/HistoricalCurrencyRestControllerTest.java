package pl.mjbladaj.zaaw_java.server.rest;

import org.joda.time.DateTime;
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
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateCalculationsService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HistoricalCurrencyRestController.class)
public class HistoricalCurrencyRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HistoricalRateService historicalRateService;

    @MockBean
    private HistoricalRateCalculationsService historicalRateCalculationsService;

    private UniversalCurrencyRateInTime getCurrencyRateInTime() {
        return UniversalCurrencyRateInTime
                .builder()
                .rate(3.4554)
                .time(TimeConverter.convertStringToDateTime(getValidDate()))
                .build();
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
    public void setUp() throws EntityNotFoundException, CurrencyNotAvailableException, TimePeriodNotAvailableException {

        Mockito.when(historicalRateService.getConvertedRateForGivenDay("EUR", "PLN", getValidDate()))
                .thenReturn(getCurrencyRateInTime());

        Mockito.when(historicalRateService.getConvertedRateForGivenPeriod("EUR", "PLN", getValidDate(), getValidFutureDate()))
                .thenReturn(getCurrencyListRateInTime());

        Mockito.when(historicalRateService.getConvertedRateForGivenPeriod("DOL", "PLN", getValidDate(), getValidFutureDate()))
                .thenThrow(new CurrencyNotAvailableException());

        Mockito.when(historicalRateService.getConvertedRateForGivenPeriod("GBP", "PLN", getValidDate(), getValidFutureDate()))
                .thenThrow(new EntityNotFoundException());

        Mockito.when(historicalRateService.getConvertedRateForGivenDay("GBP", "PLN", getInValidFutureDate()))
                .thenThrow(new TimePeriodNotAvailableException());

        Mockito.when(historicalRateService.getConvertedRateForGivenPeriod("GBP", "PLN", getValidDate(), getInValidFutureDate()))
                .thenThrow(new TimePeriodNotAvailableException());
    }

    @After
    public void tearDown() {
        Mockito.reset(historicalRateService);
    }

    @Test
    public void shouldReturnCurrencyRateInTime() throws Exception {
        mvc.perform(get("/api/currencies/EUR/" + getValidDate() + "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate", is(3.4554)));
    }

    @Test
    public void shouldReturnCurrencyRateInTimeForGivenTwoCurrencies() throws Exception {

        mvc.perform(get("/api/currencies/EUR/PLN/" + getValidDate() + "/" + getValidFutureDate()+ "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rate", is(3.4554)));
    }

    @Test
    public void shouldReturnCurrencyRateInTimeForGivenTwoCurrenciesAndStartAndEndDate() throws Exception {

        mvc.perform(get("/api/currencies/EUR/PLN/" + getValidDate() + "/" + getValidFutureDate()+ "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rate", is(3.4554)));
    }

    @Test
    public void shouldReturn404WhenApiDoNotProvidesCurrencyAndGivenDay() throws Exception {
        mvc.perform(get("/api/currencies/DOL/" + getValidDate() + "rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
    @Test
    public void shouldReturn404WhenApiDoNotProvidesCurrencyForGivenTwoCurrenciesAndStartAndEndDate() throws Exception {
        mvc.perform(get("/api/currencies/DOL/PLN/" + getValidDate() + "rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenApiDoNotProvidesCurrencyAndGivenPeriod() throws Exception {
        mvc.perform(get("/api/currencies/DOL/" + getValidDate() +"/" +getValidFutureDate() + "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
    @Test
    public void shouldReturn404WhenApiDoNotProvidesCurrencyAndGivenPeriodAndGivenTwoCurrencies() throws Exception {
        mvc.perform(get("/api/currencies/DOL/PLN/" + getValidDate() +"/" +getValidFutureDate() + "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenCurrencyIsNotAvailableAndGivenDay() throws Exception {
        mvc.perform(get("/api/currencies/GBP/" + getValidDate() + "rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenCurrencyIsNotAvailableAndGivenPeriod() throws Exception {
        mvc.perform(get("/api/currencies/GBP/" + getValidDate() +"/" +getValidFutureDate() + "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenCurrencyIsNotAvailableAndGivenPeriodAndGivenTwoCurrencies() throws Exception {
        mvc.perform(get("/api/currencies/GBP/PLN/" + getValidDate() +"/" +getValidFutureDate() + "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenTimePeriodNotAvailableAndGivenDay() throws Exception {
        mvc.perform(get("/api/currencies/GBP/" + getInValidFutureDate()+ "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenTimePeriodNotAvailableAndGivenPeriod() throws Exception {
        mvc.perform(get("/api/currencies/GBP/" + getValidDate() +"/"  + getInValidFutureDate()+ "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenTimePeriodNotAvailableAndGivenPeriodAndGivenTwoCurrencies() throws Exception {
        mvc.perform(get("/api/currencies/GBP/PLN/" + getValidDate() +"/"  + getInValidFutureDate()+ "/rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
