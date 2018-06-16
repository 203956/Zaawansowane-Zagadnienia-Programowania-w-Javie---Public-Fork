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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.dto.AverageAndDeviations;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.impl.ComplexStatisticsServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ComplexStatisticsRestController.class)
@WithMockUser
public class ComplexStatisticsRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ComplexStatisticsServiceImpl complexStatisticsService;

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

        Mockito.when(complexStatisticsService.getAverageAndDeviations("EUR", "PLN", getValidDate(), getValidFutureDate()))
                .thenReturn(AverageAndDeviations
                        .builder()
                .deviations(getDeviations())
                .average(2).build());

        Mockito.when(complexStatisticsService.getAverageAndDeviations("DOL", "EUR", getValidDate(), getValidFutureDate()))
                .thenThrow(new CurrencyNotAvailableException());

        Mockito.when(complexStatisticsService.getAverageAndDeviations("EUR", "DOL", getValidDate(), getValidFutureDate()))
                .thenThrow(new CurrencyNotAvailableException());

        Mockito.when(complexStatisticsService.getAverageAndDeviations("MVN", "DOL", getValidDate(), getValidFutureDate()))
                .thenThrow(new CurrencyNotAvailableException());

        Mockito.when(complexStatisticsService.getAverageAndDeviations("EUR", "PLN", getValidDate(), getInValidFutureDate()))
                .thenThrow(new TimePeriodNotAvailableException());
    }

    private List<Double> getDeviations() {
            List<Double> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            result.add((10 - i ) *1.0);
        }
        return result;
    }


    @After
    public void tearDown() {
        Mockito.reset(complexStatisticsService);
    }

    @Test
    public void shouldReturnCurrencyRateInTime() throws Exception {
        mvc.perform(get("/api/public/statistics/EUR/PLN/" + getValidDate() + "/" + getValidFutureDate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average", is(2.0)));
    }

    @Test
    public void shouldReturn404WhenFirstCurrencyIsNotAvailable() throws Exception {
        mvc.perform(get("/api/public/statistics/DOL/EUR/" + getValidDate() + "/" + getValidFutureDate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenSecondCurrencyIsNotAvailable() throws Exception {
        mvc.perform(get("/api/public/statistics/EUR/DOL/" + getValidDate() + "/" + getValidFutureDate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenBothCurrenciesAreNotAvailable() throws Exception {
        mvc.perform(get("/api/public/statistics/MVN/DOL/" + getValidDate() + "/" + getValidFutureDate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404WhenTimePeriodIsNotAvailable() throws Exception {
        mvc.perform(get("/api/public/statistics/EUR/PLN/" + getValidDate() + "/" + getInValidFutureDate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

}
