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
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.dao.AvailableCurrencyRepository;
import pl.mjbladaj.zaaw_java.server.dto.AccountStateData;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;

@RunWith(SpringRunner.class)
public class AccountStateServiceImplTest {

  @TestConfiguration
    static class AccountStateServiceImplTestContextConfiguration {

        @Bean
        public AccountStateService accountStateService() {
            return new AccountStateServiceImpl();
        }

        @Bean
        public AvailableCurrenciesService availableCurrenciesService() {
          return new AvailableCurrenciesServiceImpl();
      }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private AccountStateService accountStateService;

    @MockBean
    private AvailableCurrenciesService availableCurrenciesService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AvailableCurrencyRepository availableCurrencyRepository;

    @MockBean
    private AccountStateRepository accountStateRepository;

    private List<AccountState> getAllUserState() {
        Account existingAccount = Account
                .builder()
                .id(1)
                .login("exist")
                .password("{noop}pass")
                .build();

        AvailableCurrency currency = AvailableCurrency
                .builder()
                .symbol("PLN")
                .name("Złotówka polska")
                .build();

        AvailableCurrency currency1 = AvailableCurrency
                .builder()
                .symbol("USD")
                .name("Dolar amerykański")
                .build();

        ArrayList<AccountState> accountStates = new ArrayList<>();
        accountStates.add(AccountState
                .builder()
                .account(existingAccount)
                .amount(3.0)
                .availableCurrency(currency)
                .build());

        accountStates.add(AccountState
                .builder()
                .account(existingAccount)
                .amount(40.5)
                .availableCurrency(currency1)
                .build());

        return accountStates;
    }
    private void setUpAvailableCurrenciesService() {
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("PLN"))
        ))
                .thenReturn(Availability.builder().availability(true).build());
        Mockito.when(availableCurrenciesService.isAvailable(
                argThat(new StringsMatcher("MVN"))
        ))
                .thenReturn(Availability.builder().availability(false).build());
    }

    private void setUpAccountStateRepository() {
        Account existingAccount = Account
                .builder()
                .login("exist")
                .password("{noop}pass")
                .build();

        AvailableCurrency currency = AvailableCurrency
                .builder()
                .symbol("PLN")
                .name("Złotówka polska")
                .build();

        AccountState accountState = AccountState
                .builder()
                .account(existingAccount)
                .amount(0.0)
                .availableCurrency(currency)
                .build();

        Mockito.when(accountRepository
                .findByLogin("exist"))
                .thenReturn(Optional.of(existingAccount));

        Mockito.when(accountStateRepository
                .findByLoginAndSymbol("exist", "PLN"))
                .thenReturn(Optional.of(accountState));

        Mockito.when(accountStateRepository
                .findByLoginAndSymbol("new", "MVN"))
                .thenReturn(Optional.empty());

        Mockito.when(accountStateRepository
                .getAllUserAccountState("exist"))
                .thenReturn(getAllUserState());

        Mockito.when(accountStateRepository
                .getAllUserAccountState("new"))
                .thenReturn(new ArrayList<>());
    }

    @Before
    public void setUp() {
        setUpAvailableCurrenciesService();
        setUpAccountStateRepository();
    }

    @After
    public void tearDown() {
        Mockito.reset(accountStateRepository, accountRepository, availableCurrencyRepository);
    }

    @Test
    public void shouldAddMoneyToAccount() throws CurrencyNotAvailableException {
        //given
        AccountStateData accountStateData = AccountStateData
                .builder()
                .amount(34.5)
                .symbol("PLN")
                .build();
        //when
        accountStateService.addMoneyToAccount("exist", accountStateData.getSymbol(), accountStateData.getAmount());
        Optional<AccountState> accountState = accountStateRepository.findByLoginAndSymbol("exist", accountStateData.getSymbol());
        //then
        assertTrue(accountState.isPresent());
        assertEquals(accountStateData.getSymbol(), accountState.get().getAvailableCurrency().getSymbol());
        assertEquals(accountState.get().getAmount(), accountState.get().getAmount(), 0.00001);
    }

    @Test
    public void shouldThrowCurrencyNotAvailableWhenCurrencyIsNotAvailable() throws CurrencyNotAvailableException {
        //given
        AccountStateData accountStateData = AccountStateData
                .builder()
                .amount(34.5)
                .symbol("MVN")
                .build();
        //expect
        expectedException.expect(CurrencyNotAvailableException.class);
        expectedException.expectMessage("Currency is not available.");
        //when
        accountStateService.addMoneyToAccount("new", accountStateData.getSymbol(), accountStateData.getAmount());
        //then
    }

    @Test
    public void shouldGetAllUserAccountState() {
        //given
        //when
        Map<String, Double> allStates = accountStateService.getUserAccountState("exist");
        //then
        assertEquals(2, allStates.size());
        assertEquals("PLN", allStates.keySet().toArray()[0]);
        assertEquals( "USD", allStates.keySet().toArray()[1]);
        assertEquals( 3.0, allStates.get("PLN"), 0.00001);
        assertEquals( 40.5, allStates.get("USD"), 0.00001);
    }

    @Test
    public void shouldNotGetAllUserAccountState() {
        //given
        //when
        Map<String, Double> allStates = accountStateService.getUserAccountState("new");
        //then
        assertEquals(0, allStates.size());
    }
}