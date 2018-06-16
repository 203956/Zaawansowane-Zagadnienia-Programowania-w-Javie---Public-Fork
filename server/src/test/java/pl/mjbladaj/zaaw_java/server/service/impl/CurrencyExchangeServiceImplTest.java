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
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountStateException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;
import pl.mjbladaj.zaaw_java.server.service.AccountRegistrationService;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.CurrencyExchangeService;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class CurrencyExchangeServiceImplTest {
    @TestConfiguration
    static class CurrencyExchangeServiceImplTestContextConfiguration {

        @Bean
        public CurrencyExchangeService currencyExchangeService() {
            return new CurrencyExchangeServiceImpl();
        }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @MockBean
    private AccountStateRepository accountStateRepository;

    @MockBean
    private AccountStateService accountStateService;

    @MockBean
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    private AccountState fromCurrencyState;

    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("EUR", "USD", "MVC", "MVV", "PLN"))
        ))
                .thenReturn(Availability.builder().availability(true).build());

        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("MVN", "JAV"))
        ))
                .thenReturn(Availability.builder().availability(false).build());;
    }
    private void setUpAccountStateRepository() {
        fromCurrencyState = AccountState
                .builder()
                .id(1)
                .amount(10.0)
                .build();

        Mockito.when(accountStateRepository.findByLoginAndSymbol(
               argThat(new StringsMatcher( "login")),
                argThat(new StringsMatcher("USD", "MVC"))))
                .thenReturn(Optional.of(fromCurrencyState));

        Mockito.when(accountStateRepository.findByLoginAndSymbol("login", "PLN"))
                .thenReturn(Optional.empty());

        Mockito.when(accountStateRepository.findByLoginAndSymbol("login", "EUR"))
                .thenReturn(Optional.of(AccountState
                        .builder()
                        .id(2)
                        .amount(5.0)
                        .build()));
    }
    private void setUpSelectedCurrencyRateDao() throws EntityNotFoundException {
        Mockito.when(selectedCurrencyRateDao.getRate("USD", "EUR"))
                .thenReturn(UniversalRate.builder().rate(1.5).build());
        Mockito.when(selectedCurrencyRateDao.getRate("MVC", "EUR"))
                .thenThrow(new EntityNotFoundException("Currency does not exists."));
        Mockito.when(selectedCurrencyRateDao.getRate("USD", "MVC"))
                .thenThrow(new EntityNotFoundException("Currency does not exists."));
        Mockito.when(selectedCurrencyRateDao.getRate("MVC", "MVV"))
                .thenThrow(new EntityNotFoundException("Currency does not exists."));
    }
    @Before
    public void setUp() throws EntityNotFoundException {
        setUpAccountStateRepository();
        setUpAvailableCurrenciesService();
        setUpSelectedCurrencyRateDao();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.clearInvocations(availableCurrenciesService,
                accountStateRepository,
                selectedCurrencyRateDao);
    }
    @Test
    public void shouldExchangeCurrency() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //when
        currencyExchangeService
                .exchange("login", "USD", "EUR", 5);
        //then
        assertEquals(5.0, fromCurrencyState.getAmount(), 0.001);

        Mockito.verify(accountStateService)
                .addMoneyToAccount("login", "EUR", 7.5);
    }
    @Test
    public void shouldThrowAccountStateExceptionWhenNotEnoughMoney() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(AccountStateException.class);
        expectedException.expectMessage("Not enough money.");
        //when
        currencyExchangeService
                .exchange("login", "USD", "EUR", 10.01);
        //then
    }
    @Test
    public void shouldThrowAccountStateExceptionWhenNoMoney() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(AccountStateException.class);
        expectedException.expectMessage("Not enough money.");
        //when
        currencyExchangeService
                .exchange("login", "PLN", "EUR", 10.01);
        //then
    }
    @Test
    public void shouldThrowCurrencyNotAvailableExceptionWhenFirstCurrencyIsNotAvailable() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        currencyExchangeService
                .exchange("login", "MVN", "EUR", 5);
        //then
    }
    @Test
    public void shouldThrowCurrencyNotAvailableExceptionWhenSecondCurrencyIsNotAvailable() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        currencyExchangeService
                .exchange("login", "USD", "MVN", 5);
        //then
    }
    @Test
    public void shouldThrowCurrencyNotAvailableExceptionWhenBothCurrencyIsNotAvailable() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        currencyExchangeService
                .exchange("login", "MVN", "JAV", 5);
        //then
    }
    @Test
    public void shouldThrowEntityNotFoundWhenApiDoesNotProvidesFirstCurrency() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        currencyExchangeService
                .exchange("login", "MVC", "EUR", 5);
        //then
    }
    @Test
    public void shouldThrowEntityNotFoundWhenApiDoesNotProvidesSecondCurrency() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        currencyExchangeService
                .exchange("login", "USD", "MVC", 5);
        //then
    }
    @Test
    public void shouldThrowEntityNotFoundWhenApiDoesNotProvidesBothCurrency() throws AccountStateException, EntityNotFoundException, CurrencyNotAvailableException {
        //given
        //exception
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("Currency does not exists.");
        //when
        currencyExchangeService
                .exchange("login", "MVC", "MVV", 5);
        //then
    }
}