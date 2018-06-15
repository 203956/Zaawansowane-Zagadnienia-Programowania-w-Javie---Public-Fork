package pl.mjbladaj.zaaw_java.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;

import java.util.List;

public interface AccountStateRepository extends JpaRepository<AccountState, Integer> {

    @Query("SELECT a FROM AccountState a JOIN a.account ac WHERE ac.id = :accountId")
    List<AccountState> getAllUserAccountState(@Param("accountId") Integer accountId);
}
