package pl.mjbladaj.zaaw_java.server.dao;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountStateRepositoryTest {
    @After
    public void tearDown() {
        entityManager.clear();
    }

    @Autowired
    private AccountStateRepository accountStateRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldFindAccountStateByLoginAndSymbol() {
        //given
        Account account = Account
                .builder()
                .login("login")
                .password("{noop}pass")
                .build();

        AvailableCurrency currency = AvailableCurrency
                .builder()
                .symbol("MVN")
                .name("Maven dollar")
                .build();

        AccountState accountState = AccountState
                .builder()
                .account(account)
                .amount(3.21)
                .availableCurrency(currency)
                .build();
        entityManager.persist(currency);
        entityManager.persist(account);
        entityManager.persist(accountState);
        entityManager.flush();
        //when
        Optional<AccountState> accountStates =
                accountStateRepository
                        .findByLoginAndSymbol("login", "MVN" );
        //then
        assertTrue(accountStates.isPresent());
        assertEquals(account.getLogin(), accountStates.get().getAccount().getLogin());
        assertEquals(currency.getSymbol(), accountStates.get().getAvailableCurrency().getSymbol());
        assertEquals(currency.getName(), accountStates.get().getAvailableCurrency().getName());
        assertEquals(3.21, accountStates.get().getAmount(), 0.000001);
    }

    @Test
    public void shouldNotFindAccountStateByLoginAndSymbol() {
        //given
        //when
        Optional<AccountState> accountStates =
                accountStateRepository
                        .findByLoginAndSymbol("login", "MVN" );
        //then
        assertFalse(accountStates.isPresent());
    }

    @Test
    public void shouldGetAllUserAccountState() {
        //given
        Account account = Account
                .builder()
                .login("login")
                .password("{noop}pass")
                .build();

        AvailableCurrency currency = AvailableCurrency
                .builder()
                .symbol("MVN")
                .name("Maven dollar")
                .build();

        AccountState accountState = AccountState
                .builder()
                .account(account)
                .amount(3.21)
                .availableCurrency(currency)
                .build();
        entityManager.persist(currency);
        entityManager.persist(account);
        entityManager.persist(accountState);
        entityManager.flush();
        //when
        List<AccountState> accountStates =
                accountStateRepository
                        .getAllUserAccountState(account.getId());
        //then
        assertEquals(1, accountStates.size());
        assertEquals(3.21, accountStates.get(0).getAmount(), 0.000001);
        assertEquals("Maven dollar", accountStates.get(0).getAvailableCurrency().getName());
        assertEquals("MVN", accountStates.get(0).getAvailableCurrency().getSymbol());
    }

    @Test
    public void shouldNotGetAllUserAccountState() {
        //given
        Account account = Account
                .builder()
                .login("login")
                .password("{noop}pass")
                .build();
        entityManager.persist(account);
        entityManager.flush();
        //when
        List<AccountState> accountStates =
                accountStateRepository
                        .getAllUserAccountState(account.getId());
        //then
        assertEquals(0, accountStates.size());
    }
}