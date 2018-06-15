package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

@Service
public class AccountStateServiceImpl implements AccountStateService {

    @Autowired
    private AccountStateRepository accountStateRepository;


    @Override
    public void updateAccountState(Integer accountId) {
        //TODO: implement servis
       /* AccountState accountState1 = accountStateRepository.getAccountState(accountId);

        AccountState accountState;
        try {
            accountState = accountStateRepository.getAccountReferencedState(accountId, accountState1.getAvailableCurrency().getId());
            accountState.setAmount(1000.2);
        } catch(NullPointerException e) {
            accountState = new AccountState();
           // accountState.setAccount(new Account());
           // accountState.setAvailableCurrency(new AvailableCurrency());
            accountState.setAmount(1340.2);
        }

        accountStateRepository.save(accountState);*/
    }
}
