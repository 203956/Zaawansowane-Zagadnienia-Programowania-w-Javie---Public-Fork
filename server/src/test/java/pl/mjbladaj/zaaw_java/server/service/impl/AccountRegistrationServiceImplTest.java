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
import pl.mjbladaj.zaaw_java.server.converters.UserDataConverter;
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.dao.AvailableCurrencyRepository;
import pl.mjbladaj.zaaw_java.server.dto.UserRegistrationData;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.exceptions.InvalidCredentialsExeption;
import pl.mjbladaj.zaaw_java.server.exceptions.UsernameOccupiedException;
import pl.mjbladaj.zaaw_java.server.service.AccountRegistrationService;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AccountRegistrationServiceImplTest {
    @TestConfiguration
    static class AccountRegistrationServiceImplTestContextConfiguration {

        @Bean
        public AccountRegistrationService accountRegistrationService() {
            return new AccountRegistrationServiceImpl();
        }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private AccountRegistrationService accountRegistrationService;

    @MockBean
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        Account existingAccount = Account
                .builder()
                .login("exist")
                .password("{noop}pass")
                .build();

        Mockito.when(accountRepository
                .findByLogin("exist"))
                .thenReturn(Optional.of(existingAccount));

        Mockito.when(accountRepository
                .findByLogin("new"))
                .thenReturn(Optional.empty());

    }
    @After
    public void tearDown() {
        Mockito.reset(accountRepository);
    }

    @Test
    public void shouldRegisterNewUser() throws UsernameOccupiedException, InvalidCredentialsExeption {
        //given
        UserRegistrationData userRegistrationData = UserRegistrationData
                .builder()
                .username("new")
                .password("pass")
                .mail("new@domain.com")
                .build();
        Account account = UserDataConverter.getAccount(userRegistrationData);
        account.setPassword("{noop}" + userRegistrationData.getPassword());
        //when
        UserRegistrationData registrationData =
                accountRegistrationService.register(userRegistrationData);
        //then
        Mockito.verify(accountRepository)
                .save(account);
        assertEquals("encoded", registrationData.getPassword());
    }
    @Test
    public void shouldThrowUsernameOccupiedExceptionWhenUsernameIsTaken() throws UsernameOccupiedException, InvalidCredentialsExeption {
        //given
        UserRegistrationData userRegistrationData = UserRegistrationData
                .builder()
                .username("exist")
                .password("pass")
                .mail("new@domain.com")
                .build();
        //exception
        expectedException.expect(UsernameOccupiedException.class);
        expectedException.expectMessage("Username is occupied.");
        //when
        accountRegistrationService.register(userRegistrationData);
        //then
    }

    @Test
    public void shouldThrowUsernameInvalidCredentialsExceptionWhenUsernameIsTooShort() throws UsernameOccupiedException, InvalidCredentialsExeption {
        //given
        UserRegistrationData userRegistrationData = UserRegistrationData
                .builder()
                .username("a")
                .password("pass")
                .mail("new@domain.com")
                .build();
        //exception
        expectedException.expect(InvalidCredentialsExeption.class);
        expectedException.expectMessage("Username is too short.");
        //when
        accountRegistrationService.register(userRegistrationData);
        //then
    }
    @Test
    public void shouldThrowUsernameInvalidCredentialsExceptionWhenPasswordIsTooShort() throws UsernameOccupiedException, InvalidCredentialsExeption {
        //given
        UserRegistrationData userRegistrationData = UserRegistrationData
                .builder()
                .username("new")
                .password("a")
                .mail("new@domain.com")
                .build();
        //exception
        expectedException.expect(InvalidCredentialsExeption.class);
        expectedException.expectMessage("Password is too short.");
        //when
        accountRegistrationService.register(userRegistrationData);
        //then
    }
    @Test
    public void shouldThrowUsernameInvalidCredentialsExceptionWhenUsernameAndPasswordAreTooShort() throws UsernameOccupiedException, InvalidCredentialsExeption {
        //given
        UserRegistrationData userRegistrationData = UserRegistrationData
                .builder()
                .username("a")
                .password("a")
                .mail("new@domain.com")
                .build();
        //exception
        expectedException.expect(InvalidCredentialsExeption.class);
        expectedException.expectMessage("Username is too short.");
        //when
        accountRegistrationService.register(userRegistrationData);
        //then
    }
}