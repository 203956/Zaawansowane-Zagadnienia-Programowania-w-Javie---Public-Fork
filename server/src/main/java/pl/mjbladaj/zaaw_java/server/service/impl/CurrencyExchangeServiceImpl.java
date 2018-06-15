package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountStateException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.service.CurrencyExchangeService;
import pl.mjbladaj.zaaw_java.server.utils.AvailabilityUtils;

import java.util.Optional;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @Autowired
    private AccountStateRepository accountStateRepository;

    @Autowired
    private AccountStateService accountStateService;

    @Autowired
    private SelectedCurrencyRateDao selectedCurrencyRateDao;

    @Override
    public void exchange(String username, String fromCurrency, String toCurrency, double amount) throws CurrencyNotAvailableException, AccountStateException {
        AvailabilityUtils.checkAvailability(availableCurrenciesService, toCurrency);
        AvailabilityUtils.checkAvailability(availableCurrenciesService, fromCurrency);
        checkAccountState(username, fromCurrency, amount);

        Optional<AccountState> accountStateFrom = accountStateRepository
                .findByLoginAndSymbol(username, fromCurrency);

        accountStateFrom
                .get()
                .setAmount(
                        accountStateFrom.get().getAmount() - amount
                );
        //TODO:
        //Exchange currency
        accountStateService.addMoneyToAccount(
                username, toCurrency, amount
        );
    }

    private void checkAccountState(String username, String fromCurrency, double amount) throws AccountStateException {
        Optional<AccountState> accountState = accountStateRepository
                .findByLoginAndSymbol(username, fromCurrency);
        if(accountState.isPresent()) {
            if(accountState.get().getAmount() < amount)
                throw new AccountStateException("Not enough money");
        } else {
            throw new AccountStateException("Not enough money");
        }
    }
}
