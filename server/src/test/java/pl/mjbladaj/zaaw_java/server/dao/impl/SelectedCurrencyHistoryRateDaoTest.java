package pl.mjbladaj.zaaw_java.server.dao.impl;

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
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.ClassMatcher;
import pl.mjbladaj.zaaw_java.server.RateGenerator;
import pl.mjbladaj.zaaw_java.server.RateInTimeGenerator;
import pl.mjbladaj.zaaw_java.server.StringsMatcher;
import pl.mjbladaj.zaaw_java.server.converters.RateInTimeConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dao.impl.SelectedCurrencyHistoryRateDaoImpl;
import pl.mjbladaj.zaaw_java.server.dao.impl.SelectedCurrencyRateDaoImpl;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRate;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRateInTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class SelectedCurrencyHistoryRateDaoTest {

    private static String BASE_URL = "https://example.com/convert?q=";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private Environment environment;

    @TestConfiguration
    static class SelectedCurrencyHistoryRateDaoImplTestContextConfiguration {
        @Bean
        public SelectedCurrencyHistoryRateDao selectedCurrencyHistoryRateDao() {
            return new SelectedCurrencyHistoryRateDaoImpl();
        }
    }

    private String getValidUrl() {
        return BASE_URL + "PLN_USD" + "&date=" + getValidDate();
    }

    private String[] getInvalidUrl() {
        return new String[] {
                BASE_URL + "DOL_PLN",
                BASE_URL + "PLN_DOL",
                BASE_URL + "DOL_DCL",
        };
    }

    private String getValidDate() {return "2018-01-20"; }

    @Before
    public void setUp() throws Exception {
        Mockito.when(environment.getProperty("exchange.currency.base.url"))
                .thenReturn(BASE_URL);

        Mockito.when(restTemplate
                .getForEntity(getValidUrl(), FreeCurrenciesComRateInTime.class))
                .thenReturn(ResponseEntity.ok(RateInTimeGenerator.getRateInTime()));

        Mockito.when(restTemplate
                .getForEntity(
                        argThat(new StringsMatcher(getInvalidUrl())),
                        argThat(new ClassMatcher(FreeCurrenciesComRate.class))))
                .thenReturn(ResponseEntity.ok(RateGenerator.getEmptyRate()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldFindGivenCurrencyRate() throws TimePeriodNotAvailableException, EntityNotFoundException {
        //given
        String fromCurrency = "PLN";
        String toCurrency = "USD";
        String date = "2018-01-20";
        //when
        UniversalCurrencyRateInTime rateInTime = selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, date);
        //then
        assertEquals(4.1582, rateInTime.getRate(), 0.00001);
    }

    /* UniversalCurrencyRateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date) throws TimePeriodNotAvailableException, EntityNotFoundException;
    List<UniversalCurrencyRateInTime> getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate) throws TimePeriodNotAvailableException, EntityNotFoundException;
*/
}
