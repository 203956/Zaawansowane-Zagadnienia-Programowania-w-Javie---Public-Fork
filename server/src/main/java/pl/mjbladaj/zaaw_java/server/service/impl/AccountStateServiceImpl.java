package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.converters.AccountStateConverter;
import pl.mjbladaj.zaaw_java.server.dao.AccountStateRepository;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

import java.util.List;
import java.util.Map;

@Service
public class AccountStateServiceImpl implements AccountStateService {

    @Autowired
    private AccountStateRepository accountStateRepository;

    @Override
    public Map<String, Double> getAllUserAccountState(Integer accountId) {
        List<AccountState> accountStates = accountStateRepository.getAllUserAccountState(accountId);
        return AccountStateConverter.getAccountStateDto(accountStates);
    }
}
