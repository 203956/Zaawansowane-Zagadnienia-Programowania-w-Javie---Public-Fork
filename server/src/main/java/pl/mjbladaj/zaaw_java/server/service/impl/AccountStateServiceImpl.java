package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

@Service
public class AccountStateServiceImpl implements AccountStateService {

    @Autowired
    private AccountStateRepository accountStateRepository;


    @Override
    public void updateAccountState(Integer accountId, Double amount) {
        AccountState accountState = accountStateRepository.getAccountState(accountId);

        if (accountState.getAmount() == null) {
            accountState.setAmount(amount);
        } else {
            double prevAmount = accountState.getAmount();
            accountState.setAmount(prevAmount + amount);
        }

        accountStateRepository.save(accountState);
    }
}
