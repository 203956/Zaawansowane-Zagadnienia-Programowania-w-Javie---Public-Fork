package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountNotFoundException;

public interface AccountService {
    Account getAccount(String login) throws AccountNotFoundException;
}
