package pl.mjbladaj.zaaw_java.server.dao;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.entity.Currency;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CurrencyRepositoryTest {

    @After
    public void tearDown() {
        entityManager.clear();
    }

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldFindCurrency() {
        //given
        Currency currency = Currency
                .builder()
                .symbol("PLN")
                .name("Polish zloty")
                .build();
        entityManager.persist(currency);
        entityManager.flush();
        //when
        Optional<Currency> founded = currencyRepository.findBySymbol("PLN");
        //then
        assertTrue(founded.isPresent());
        assertEquals("Polish zloty", founded.get().getName());
    }
    @Test
    public void shouldNotFindCurrency() {
        //given
        //when
        Optional<Currency> founded = currencyRepository.findBySymbol("PLN");
        //then
        assertFalse(founded.isPresent());
    }
}