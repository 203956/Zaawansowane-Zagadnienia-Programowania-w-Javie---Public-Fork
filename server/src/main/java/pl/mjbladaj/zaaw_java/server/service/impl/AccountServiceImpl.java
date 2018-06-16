package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountNotFoundException;
import pl.mjbladaj.zaaw_java.server.service.AccountService;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account getAccount(String login) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByLogin(login);
        checkAccountPresence(optionalAccount);
        return optionalAccount.get();
    }

    private void checkAccountPresence(Optional<Account> accountOptional) throws AccountNotFoundException {
        if(!accountOptional.isPresent()) {
            throw new AccountNotFoundException("Account not found.");
        }
    }
}
