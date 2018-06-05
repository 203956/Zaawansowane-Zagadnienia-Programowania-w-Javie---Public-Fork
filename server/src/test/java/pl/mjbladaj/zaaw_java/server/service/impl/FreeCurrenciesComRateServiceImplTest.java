package pl.mjbladaj.zaaw_java.server.service.impl;

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
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.StringsMatcher;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.RateService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class FreeCurrenciesComRateServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @MockBean
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Autowired
    private RateService rateService;

    @TestConfiguration
    static class FreeCurrenciesComRateServiceImplTestContextConfiguration {
        @Bean
        public RateService rateService() {
            return new RateServiceImpl();
        }
    }


    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("PLN", "EUR", "DOL", "DCL"))
        ))
                .thenReturn(Availability.builder().availability(true).build());
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("MVN", "JAV"))
        ))
                .thenReturn(Availability.builder().availability(false).build());
    }

    @Before
    public void setUp() throws Exception {
        setUpAvailableCurrenciesService();
        setUpSelectedCurrencyRateDao();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(selectedCurrencyRateDao, availableCurrenciesService);
    }

    private void setUpSelectedCurrencyRateDao() throws EntityNotFoundException {
        Mockito.when(selectedCurrencyRateDao.getRate("EUR", "PLN"))
                .thenReturn(UniversalRate
                        .builder()
                        .symbol("EUR")
                        .rate(4.6522)
                        .build());
    }

    @Test
    public void shouldReturnValidCurrencyRate() throws CurrencyNotAvailableException, EntityNotFoundException {
        //given
        //when
        CurrencyRate convertedRate = rateService.getConvertedRate("EUR", "PLN");
        //then
        assertEquals(4.6522, convertedRate.getRate(), 0.00001);
    }


}