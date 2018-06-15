package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.mjbladaj.zaaw_java.server.converters.UserDataConverter;
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.dto.UserRegistrationData;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.exceptions.UsernameOccupiedException;
import pl.mjbladaj.zaaw_java.server.service.AccountRegistrationService;

@Transactional(isolation = Isolation.SERIALIZABLE)
@Service
public class AccountRegistrationServiceImpl implements AccountRegistrationService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserRegistrationData register(UserRegistrationData userRegistrationData) throws UsernameOccupiedException {
        if(accountRepository
                .findByLogin(userRegistrationData.getUsername())
                .isPresent())
            throw new UsernameOccupiedException("Username is occupied");

        Account newAccount = UserDataConverter
                .getAccount(userRegistrationData);

        newAccount.setPassword(
                "{noop}" + newAccount.getPassword()
        );
        accountRepository.save(newAccount);
        userRegistrationData.setId(newAccount.getId());
        return  userRegistrationData;
    }
}
