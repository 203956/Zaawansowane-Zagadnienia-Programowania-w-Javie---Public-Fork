package pl.mjbladaj.zaaw_java.server.secruity;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.entity.Account;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AccountAuthenticationServiceTest {

    @TestConfiguration
    static class AccountAuthenticationServiceTestContextConfiguration {

        @Bean
        public AccountAuthenticationService accountAuthenticationService() {
            return new AccountAuthenticationService();
        }
    }

    @Autowired
    private AccountAuthenticationService accountAuthenticationService;

    @MockBean
    private AccountRepository accountRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        Account account = Account
                .builder()
                .login("login")
                .password("{noop}pass1")
                .build();

        Mockito.when(accountRepository
                .findByLogin("login"))
                .thenReturn(Optional.of(account));

        Mockito.when(accountRepository
                .findByLogin("login2"))
                .thenReturn(Optional.empty());
    }
    @After
    public void tearDown() {
        Mockito.reset(accountRepository);
    }
    @Test
    public void shouldFindUser() {
        //given
        //when
        UserDetails userDetails = accountAuthenticationService
                .loadUserByUsername("login");
        //then
        assertEquals("login", userDetails.getUsername());
        assertEquals("{noop}pass1", userDetails.getPassword());
    }
    @Test
    public void shouldNotFindUserWhenLoginIsWrong() {
        //given
        //exception
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage("Could not find user login1");
        //when
        accountAuthenticationService.loadUserByUsername("login1");
        //then
    }
}