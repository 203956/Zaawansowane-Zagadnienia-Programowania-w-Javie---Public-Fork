package pl.mjbladaj.zaaw_java.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;

import java.util.Optional;

public interface AvailableCurrencyRepository extends JpaRepository<AvailableCurrency, Integer> {

    Optional<AvailableCurrency> findBySymbol(String symbol);
}
