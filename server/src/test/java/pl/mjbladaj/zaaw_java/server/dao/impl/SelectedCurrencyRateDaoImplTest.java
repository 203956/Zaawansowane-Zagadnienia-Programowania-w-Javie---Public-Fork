package pl.mjbladaj.zaaw_java.server.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import pl.mjbladaj.zaaw_java.server.models.Rate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class SelectedCurrencyRateDaoImplTest {

    private static String BASE_URL = "https://example.com/convert?q=";

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private Environment environment;

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @TestConfiguration
    static class SelectedCurrencyRateDaoImplTestContextConfiguration {
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
            BASE_URL + "EUR_DOL",
            BASE_URL + "DOL_MVN",
        };
    }
    @Before
    public void setUp() throws Exception {
        Mockito.when(environment.getProperty("exchange.currency.base.url"))
                .thenReturn(BASE_URL);

        Mockito.when(restTemplate
                .getForEntity(getValidUrl(), Rate.class))
                .thenReturn(ResponseEntity.ok(RateGenerator.getRate()));

        Mockito.when(restTemplate
                .getForEntity(
                        argThat(new StringsMatcher(getInvalidUrl())),
                        argThat(new ClassMatcher(Rate.class))))
                .thenReturn(ResponseEntity.ok(RateGenerator.getEmptyRate()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldReturnValidCurrencyRate() {
        //given
        //when
        System.out.println(getValidUrl());
        Rate rate = selectedCurrencyRateDao.getRate("EUR", "PLN");
        //then
        assertEquals(4.6522, (double) rate.getResults().get("EUR_PLN").get("val"), 0.00001);
    }

    @Test
    public void shouldReturnEmptyRateWhenFirstCurrencyIsNotProvidedByApi() {
        //given
        //when
        Rate rate = selectedCurrencyRateDao.getRate("DOL", "PLN");
        //then
        assertTrue(rate.getQuery().isEmpty());
        assertTrue(rate.getResults().isEmpty());
    }
    @Test
    public void shouldReturnEmptyRateWhenSecondCurrencyIsNotProvidedByApi() {
        //given
        //when
        Rate rate = selectedCurrencyRateDao.getRate("EUR", "DOL");
        //then
        assertTrue(rate.getQuery().isEmpty());
        assertTrue(rate.getResults().isEmpty());
    }
    @Test
    public void shouldReturnEmptyRateWhenBothCurrencyIsNotProvidedByApi() {
        //given
        //when
        Rate rate = selectedCurrencyRateDao.getRate("DOL", "MVN");
        //then
        assertTrue(rate.getQuery().isEmpty());
        assertTrue(rate.getResults().isEmpty());
    }
}