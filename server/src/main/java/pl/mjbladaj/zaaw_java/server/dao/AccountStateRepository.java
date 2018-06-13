package pl.mjbladaj.zaaw_java.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;

public interface AccountStateRepository extends JpaRepository<AccountState, Integer> {
    //TODO create query methods
}
