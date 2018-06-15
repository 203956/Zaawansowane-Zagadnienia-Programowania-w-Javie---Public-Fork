package pl.mjbladaj.zaaw_java.server.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.mjbladaj.zaaw_java.server.dao.AccountRepository;
import pl.mjbladaj.zaaw_java.server.entity.Account;

import java.util.Optional;

@Service
public class AccountAuthenticationService implements UserDetailsService  {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository
                .findByLogin(username);

        if(account.isPresent()) {
            return User
                    .withUsername(account.get().getLogin())
                    .password(account.get().getPassword())
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("Could not find user "
                    + username);
        }
    }

}
