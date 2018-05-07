package pl.mjbladaj.zaaw_java.server.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.dao.AvailableCurrencyRepository;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AvailableCurrenciesServiceImplTest {
    @TestConfiguration
    static class AvailableCurrenciesServiceImplTestContextConfiguration {

        @Bean
        public AvailableCurrenciesService currenciesService() {
            return new AvailableCurrenciesServiceImpl();
        }
    }

    @Autowired
    private AvailableCurrenciesService currenciesService;

    @MockBean
    private AvailableCurrencyRepository availableCurrencyRepository;

    private List<AvailableCurrency> getAvailableCurrencies() {
        ArrayList<AvailableCurrency> currencies = new ArrayList<>();
        currencies.add(AvailableCurrency
        .builder()
        .symbol("GBP")
        .build());

        currencies.add(AvailableCurrency
                .builder()
                .symbol("EUR")
                .build());

        currencies.add(AvailableCurrency
                .builder()
                .symbol("DOL")
                .build());

        return currencies;
    }

    @Before
    public void setUp() {
        Mockito.when(availableCurrencyRepository.findAll())
                .thenReturn(getAvailableCurrencies());

        Mockito.when(availableCurrencyRepository.findBySymbol("PLN"))
                .thenReturn(Optional.of(
                        AvailableCurrency.builder().symbol("PLN").build()
                ));
        Mockito.when(availableCurrencyRepository.findBySymbol("MVN"))
                .thenReturn(Optional.empty());
    }
    @After
    public void tearDown() {
        Mockito.reset(availableCurrencyRepository);
    }

    @Test
    public void shouldReturnListOfAvailableCurrencies() {
        //given
        //when
        List<AvailableCurrency> available = currenciesService.getAll();
        //then
        assertEquals(3, available.size());

        assertEquals("GBP", available.get(0).getSymbol());
        assertEquals("EUR", available.get(1).getSymbol());
        assertEquals("DOL", available.get(2).getSymbol());
    }
    @Test
    public void shouldReturnEmptyListOfAvailableCurrencies() {
        //given
        Mockito.reset(availableCurrencyRepository);
        //when
        List<AvailableCurrency> available = currenciesService.getAll();
        //then
        assertEquals(0, available.size());
    }
    @Test
    public void shouldReturnTrueWhenCurrencyIsAvailable() {
        //given
        //when
        Availability available = currenciesService.isAvailable("PLN");
        //then
        assertTrue(available.isAvailability());
    }
    @Test
    public void shouldReturnFalseWhenCurrencyIsNotAvailable() {
        //given
        //when
        Availability available = currenciesService.isAvailable("MVN");
        //then
        assertFalse(available.isAvailability());
    }
}