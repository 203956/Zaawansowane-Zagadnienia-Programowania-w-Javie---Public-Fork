package pl.mjbladaj.zaaw_java.server.dao;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.entity.Account;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
    @After
    public void tearDown() {
        entityManager.clear();
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void shouldFindUserByLogin() {
        //given
        Account account = Account
                .builder()
                .login("login")
                .password("{noop}pass")
                .build();
        entityManager.persist(account);
        entityManager.flush();
        //when
        Optional<Account> optionalAccount =
                accountRepository
                        .findByLogin("login");
        //then
        assertTrue(optionalAccount.isPresent());
        assertEquals(optionalAccount.get().getId(), account.getId());
    }
    @Test
    public void shouldNotFindUserByLogin() {
        //given
        //when
        Optional<Account> optionalAccount =
                accountRepository
                        .findByLogin("login");
        //then
        assertFalse(optionalAccount.isPresent());
    }
}