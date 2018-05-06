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
import pl.mjbladaj.zaaw_java.server.StringsMatcher;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.models.FreeCurrenciesComRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class SelectedCurrencyFreeCurrenciesComRateDaoImplTest {

    private static String BASE_URL = "https://example.com/convert?q=";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private Environment environment;

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @TestConfiguration
    static class SelectedCurrencyFreeCurrenciesComRateDaoImplTestContextConfiguration {
        @Bean
        public SelectedCurrencyRateDao selectedCurrencyRateDao() {
            return new SelectedCurrencyRateDaoImpl();
        }
    }

    private String getValidUrl() {
        return BASE_URL + "EUR_PLN";
    }
    private String[] getInvalidUrl() {
        return new String[] {
            BASE_URL + "DOL_PLN",
            BASE_URL + "PLN_DOL",
            BASE_URL + "DOL_DCL",
        };
    }
    @Before
    public void setUp() throws Exception {
        Mockito.when(environment.getProperty("exchange.currency.base.url"))
                .thenReturn(BASE_URL);

        Mockito.when(restTemplate
                .getForEntity(getValidUrl(), FreeCurrenciesComRate.class))
                .thenReturn(ResponseEntity.ok(RateGenerator.getRate()));

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
    public void shouldReturnValidCurrencyRate() throws EntityNotFoundException {
        //given
        //when
        UniversalRate rate = selectedCurrencyRateDao.getRate("EUR", "PLN");
        //then
        assertEquals("EUR", rate.getSymbol());
        assertEquals(4.6522, rate.getRate(), 0.00001);
    }
    @Test
    public void shouldThrowEntityNotFoundWhenFirstCurrencyIsNotProvidedByApi() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        UniversalRate convertedRate = selectedCurrencyRateDao.getRate("DOL", "PLN");
        //then
    }

    @Test
    public void shouldThrowEntityNotFoundWhenSecondCurrencyIsNotProvidedByApi() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        UniversalRate convertedRate = selectedCurrencyRateDao.getRate("PLN", "DOL");
        //then
    }

    @Test
    public void shouldThrowEntityNotFoundWhenBothCurrencyIsNotProvidedByApi() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //expect
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        UniversalRate convertedRate = selectedCurrencyRateDao.getRate("DOL", "DCL");
        //then
    }
}