package pl.mjbladaj.zaaw_java.server.dao.impl;

import org.joda.time.DateTime;
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
import pl.mjbladaj.zaaw_java.server.converters.TimeConverter;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyHistoryRateDao;
import pl.mjbladaj.zaaw_java.server.dao.impl.SelectedCurrencyHistoryRateDaoImpl;
import pl.mjbladaj.zaaw_java.server.dao.impl.SelectedCurrencyRateDaoImpl;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRate;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRateInTime;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class SelectedCurrencyHistoryRateDaoTest {

    private static String BASE_URL = "https://example.com/convert?q=";
    private static String MAX_DAYS = "10";
    private static String MAX_PERIOD = "7";
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
    private String getValidUrlForPeriod() {
        return BASE_URL + "PLN_USD" + "&date=" + getValidDate()
                + "&endDate=" +getValidFutureDate() ;
    }

    private String[] getInvalidUrl() {
        return new String[] {
                BASE_URL + "DOL_PLN"  + "&date=" + getValidDate(),
                BASE_URL + "PLN_DOL"  + "&date=" + getValidDate(),
                BASE_URL + "DOL_DCL"  + "&date=" + getValidDate(),
        };
    }
    private String[] getInvalidUrlForPeriod() {
        return new String[] {
                BASE_URL + "DOL_PLN"  + "&date=" + getValidDate() + "&endDate=" + getValidFutureDate(),
                BASE_URL + "PLN_DOL"  + "&date=" + getValidDate()+ "&endDate=" + getValidFutureDate(),
                BASE_URL + "DOL_DCL"  + "&date=" + getValidDate()+ "&endDate=" + getValidFutureDate(),
        };
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
    public void setUp() throws Exception {
        Mockito.when(environment.getProperty("exchange.currency.base.url"))
                .thenReturn(BASE_URL);
        Mockito.when(environment.getProperty("exchange.currency.base.max.history"))
                .thenReturn(MAX_DAYS);
        Mockito.when(environment.getProperty("exchange.currency.base.max.period"))
                .thenReturn(MAX_PERIOD);



        Mockito.when(restTemplate
                .getForEntity(getValidUrl(), FreeCurrenciesComRateInTime.class))
                .thenReturn(ResponseEntity.ok(RateInTimeGenerator.getRateInTime(1)));

        Mockito.when(restTemplate
                .getForEntity(getValidUrlForPeriod(), FreeCurrenciesComRateInTime.class))
                .thenReturn(ResponseEntity.ok(RateInTimeGenerator.getRateInTime(4)));

        Mockito.when(restTemplate
                .getForEntity(
                        argThat(new StringsMatcher(getInvalidUrl())),
                        argThat(new ClassMatcher(FreeCurrenciesComRateInTime.class))))
                .thenReturn(ResponseEntity.ok(RateInTimeGenerator.getEmptyRateInTime()));

        Mockito.when(restTemplate
                .getForEntity(
                        argThat(new StringsMatcher(getInvalidUrlForPeriod())),
                        argThat(new ClassMatcher(FreeCurrenciesComRateInTime.class))))
                .thenReturn(ResponseEntity.ok(RateInTimeGenerator.getEmptyRateInTime()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldFindGivenCurrencyRate() throws TimePeriodNotAvailableException, EntityNotFoundException {
        //given
        String fromCurrency = "PLN";
        String toCurrency = "USD";
        String date = getValidDate();
        //when
        UniversalCurrencyRateInTime rateInTime = selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, date);
        //then
        assertEquals(4.1582, rateInTime.getRate(), 0.00001);
    }
    @Test
    public void shouldThrowTimePeriodNotAvailableException() throws TimePeriodNotAvailableException, EntityNotFoundException {
        //given
        String fromCurrency = "PLN";
        String toCurrency = "USD";
        String date = "2018-01-20";
        //expect
        expectedException.expect(TimePeriodNotAvailableException.class);
        //when
        UniversalCurrencyRateInTime rateInTime = selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, date);
        //then
    }
    /* UniversalCurrencyRateInTime getGivenDayRate(String fromCurrency, String toCurrency, String date) throws TimePeriodNotAvailableException, EntityNotFoundException;
    List<UniversalCurrencyRateInTime> getGivenPeriodRate(String fromCurrency, String toCurrency, String startDate, String endDate) throws TimePeriodNotAvailableException, EntityNotFoundException;
*/
    @Test
    public void shouldThrowEntityNotFoundExceptionWhenBothCurrenciesDoesNotExists() throws TimePeriodNotAvailableException, EntityNotFoundException {

        String fromCurrency = "DOL";
        String toCurrency = "DCL";
        DateTime date = new DateTime();
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exist.");
        //when
        UniversalCurrencyRateInTime rateInTime = selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, TimeConverter.convertDateToString(date));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenSecondDoesNotExists() throws TimePeriodNotAvailableException, EntityNotFoundException {

        String fromCurrency = "PLN";
        String toCurrency = "DOL";
        DateTime date = new DateTime();
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exist.");
        //when
        UniversalCurrencyRateInTime rateInTime = selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, TimeConverter.convertDateToString(date));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenFirstDoesNotExist() throws TimePeriodNotAvailableException, EntityNotFoundException {

        String fromCurrency = "DOL";
        String toCurrency = "PLN";
        DateTime date = new DateTime();
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exist.");
        //when
        UniversalCurrencyRateInTime rateInTime = selectedCurrencyHistoryRateDao.getGivenDayRate(fromCurrency, toCurrency, TimeConverter.convertDateToString(date));
    }

    @Test
    public void shouldFindGivenPeriodCurrencyRate() throws TimePeriodNotAvailableException, EntityNotFoundException {
        //given
        String fromCurrency = "PLN";
        String toCurrency = "USD";
        String date = getValidDate();
        String endDate = getValidFutureDate();
        //when
        List<UniversalCurrencyRateInTime> ratesInTime = selectedCurrencyHistoryRateDao.getGivenPeriodRate(fromCurrency, toCurrency, date, endDate);
        //then
        assertEquals(4.1582, ratesInTime.get(0).getRate(), 0.00001);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenFirstCurrencyDoesNotExist() throws TimePeriodNotAvailableException, EntityNotFoundException {
        //given
        String fromCurrency = "DOL";
        String toCurrency = "PLN";
        String date = getValidDate();
        String endDate = getValidFutureDate();
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exist.");
        //when
        List<UniversalCurrencyRateInTime> ratesInTime = selectedCurrencyHistoryRateDao.getGivenPeriodRate(fromCurrency, toCurrency, date, endDate);
        //then
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenSecondCurrencyDoesNotExist() throws TimePeriodNotAvailableException, EntityNotFoundException {
        //given
        String fromCurrency = "PLN";
        String toCurrency = "DOL";
        String date = getValidDate();
        String endDate = getValidFutureDate();
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exist.");
        //when
        List<UniversalCurrencyRateInTime> ratesInTime = selectedCurrencyHistoryRateDao.getGivenPeriodRate(fromCurrency, toCurrency, date, endDate);
        //then
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenBothCurrenciesDoesNotExist() throws TimePeriodNotAvailableException, EntityNotFoundException {
        //given
        String fromCurrency = "DOL";
        String toCurrency = "DCL";
        String date = getValidDate();
        String endDate = getValidFutureDate();
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exist.");
        //when
        List<UniversalCurrencyRateInTime> ratesInTime = selectedCurrencyHistoryRateDao.getGivenPeriodRate(fromCurrency, toCurrency, date, endDate);
        //then
    }

}
