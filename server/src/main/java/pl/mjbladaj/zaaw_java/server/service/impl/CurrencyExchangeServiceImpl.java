package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import pl.mjbladaj.zaaw_java.server.dto.ExchangeStatus;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountStateException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
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
    public ExchangeStatus exchange(String username, String fromCurrency, String toCurrency, double amount) throws CurrencyNotAvailableException, AccountStateException, EntityNotFoundException {
        check(username, fromCurrency, toCurrency, amount);

        Optional<AccountState> accountStateFrom = accountStateRepository
                .findByLoginAndSymbol(username, fromCurrency);

        accountStateFrom
                .get()
                .setAmount(
                    safeSub(accountStateFrom.get().getAmount(), amount)
                );
        double rate = getRate(fromCurrency, toCurrency);
        double exchangedAmount = safeMul(amount, rate);
        accountStateService.addMoneyToAccount(
                username, toCurrency, exchangedAmount
        );
        return getExchangeStatus(fromCurrency, amount, toCurrency, exchangedAmount);
    }
    private double safeSub(double a, double b) {
        return ((int)(100 * a) - (int)(100 * b)) / 100.0;
    }
    private double safeMul(double amount, double rate) {
        return roundCurrencyAmount(
                ((int)(100 * amount) * (int)(1000000 * rate))
                / 100000000.0);
    }
    private ExchangeStatus getExchangeStatus(String fromCurrency, double fromCurrencyAmount,
                                   String toCurrency, double toCurrencyAmount) {
        return ExchangeStatus
                .builder()
                .fromCurrency(fromCurrency)
                .fromCurrencyAmount(fromCurrencyAmount)
                .toCurrency(toCurrency)
                .toCurrencyAmount(toCurrencyAmount)
                .build();
    }
    private void check(String username, String fromCurrency, String toCurrency, double amount) throws CurrencyNotAvailableException, AccountStateException {
        AvailabilityUtils.checkAvailability(availableCurrenciesService, toCurrency);
        AvailabilityUtils.checkAvailability(availableCurrenciesService, fromCurrency);
        checkAccountState(username, fromCurrency, amount);
    }
    private double getRate(String fromCurrency, String toCurrency) throws EntityNotFoundException {
        return selectedCurrencyRateDao
                .getRate(fromCurrency, toCurrency)
                .getRate()
                .doubleValue();
    }
    private double roundCurrencyAmount(double amount) {
        return ((int)(amount * 100)) / 100.0;
    }
    private void checkAccountState(String username, String fromCurrency, double amount) throws AccountStateException {
        Optional<AccountState> accountState = accountStateRepository
                .findByLoginAndSymbol(username, fromCurrency);
        if(accountState.isPresent()) {
            if(accountState.get().getAmount() < amount)
                throw new AccountStateException("Not enough money.");
        } else {
            throw new AccountStateException("Not enough money.");
        }
    }
}
