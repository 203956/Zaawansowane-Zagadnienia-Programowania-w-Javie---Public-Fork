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
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountNotFoundException;
import pl.mjbladaj.zaaw_java.server.service.AccountService;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AccountServiceImplTest {

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {

        @Bean
        public AccountService accountStateService() {
            return new AccountServiceImpl();
        }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    private Account getAccount() {
        return Account
                .builder()
                .id(1)
                .login("login3")
                .password("{noop}pass3")
                .mail("login3@domain.com")
                .build();
    }

    @Before
    public void setUp() {
        Mockito.when(accountRepository.findByLogin("login3"))
                .thenReturn(Optional.of(getAccount()));

        Mockito.when(accountRepository.findByLogin("login4"))
                .thenReturn(Optional.empty());
    }

    @After
    public void tearDown() {
        Mockito.reset(accountRepository);
    }

    @Test
    public void shouldReturnAccount() throws AccountNotFoundException {
        //given
        //when
        Account account = accountService.getAccount("login3");
        //then
        assertEquals(1, account.getId().intValue());
        assertEquals("login3", account.getLogin());
        assertEquals("login3@domain.com", account.getMail());
        assertEquals("{noop}pass3", account.getPassword());
    }

    @Test
    public void shouldThrowAccountNotFoundException() throws AccountNotFoundException {
        //given
        expectedException.expect(AccountNotFoundException.class);
        expectedException.expectMessage("Account not found.");
        //when
        Account account = accountService.getAccount("login4");
        //then
    }
}

