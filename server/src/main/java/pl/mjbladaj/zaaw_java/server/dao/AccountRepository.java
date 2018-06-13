package pl.mjbladaj.zaaw_java.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mjbladaj.zaaw_java.server.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    //TODO create query methods
}
