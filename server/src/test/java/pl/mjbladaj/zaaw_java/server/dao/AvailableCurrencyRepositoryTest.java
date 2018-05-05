package pl.mjbladaj.zaaw_java.server.dao;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.dao.impl.CurrencyExchangeImpl;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AvailableCurrencyRepositoryTest {

    @After
    public void tearDown() {
        entityManager.clear();
    }

    @Autowired
    private AvailableCurrencyRepository availableCurrencyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldFindCurrency() {
        //given
        AvailableCurrency currency = AvailableCurrency
                .builder()
                .symbol("PLN")
                .name("Polish zloty")
                .build();
        entityManager.persist(currency);
        entityManager.flush();
        //when
        Optional<AvailableCurrency> founded = availableCurrencyRepository.findBySymbol("PLN");
        //then
        assertTrue(founded.isPresent());
        assertEquals("Polish zloty", founded.get().getName());
    }
    @Test
    public void shouldNotFindCurrency() {
        //given
        //when
        Optional<AvailableCurrency> founded = availableCurrencyRepository.findBySymbol("PLN");
        //then
        assertFalse(founded.isPresent());
    }
    @Autowired
    CurrencyExchangeRate currencyExchangeRate;

    @Test
    public void should() {


        assertEquals(0, currencyExchangeRate.getActualExchangeRate() );
    }
}