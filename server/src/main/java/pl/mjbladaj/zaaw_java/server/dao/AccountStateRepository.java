package pl.mjbladaj.zaaw_java.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;

import java.util.Optional;

import java.util.List;

public interface AccountStateRepository extends JpaRepository<AccountState, Integer> {

    @Query("SELECT accSt FROM AccountState accSt " +
            "JOIN accSt.account ac JOIN accSt.availableCurrency av " +
            "WHERE ac.login = :login AND av.symbol = :symbol")
    Optional<AccountState> findByLoginAndSymbol(
            @Param("login") String login,
            @Param("symbol") String symbol);

    @Query("SELECT a FROM AccountState a JOIN a.account ac WHERE ac.login = :login")
    List<AccountState> getAllUserAccountState(@Param("login") String login);
}
