package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.entity.Account;

public interface AccountService {
    Account getAccount(String login);
}
