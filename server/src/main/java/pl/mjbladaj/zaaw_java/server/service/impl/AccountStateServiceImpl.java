package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.AccountStateConverter;
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.dao.AvailableCurrencyRepository;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;
import pl.mjbladaj.zaaw_java.server.utils.AvailabilityUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
public class AccountStateServiceImpl implements AccountStateService {

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @Autowired
    private AccountStateRepository accountStateRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AvailableCurrencyRepository availableCurrencyRepository;

    @Override
    public void addMoneyToAccount(String username, String currency, double amount) throws CurrencyNotAvailableException {
        AvailabilityUtils.checkAvailability(availableCurrenciesService, currency);

        Optional<AccountState> accountState =
                accountStateRepository
                        .findByLoginAndSymbol(username, currency);
        if (accountState.isPresent()) {
            accountState.get().setAmount(
                    accountState.get().getAmount() + amount
            );
        } else {
            accountState = getNewAccountState(username, currency, amount);
        }

        accountStateRepository.save(accountState.get());
    }

    public Map<String, Double> getUserAccountState(String username) {
        List<AccountState> accountStates = accountStateRepository
                .getAllUserAccountState(username);
        return AccountStateConverter.getAccountStateDto(accountStates);
    }

    private Optional<AccountState> getNewAccountState(String username, String currency, double amount) {
        Account account = accountRepository.findByLogin(username).get();
        AvailableCurrency availableCurrency = availableCurrencyRepository.findBySymbol(currency).get();

        return Optional.of(
          AccountState
          .builder()
          .account(account)
          .availableCurrency(availableCurrency)
          .amount(amount)
          .build()
        );
    }
}
