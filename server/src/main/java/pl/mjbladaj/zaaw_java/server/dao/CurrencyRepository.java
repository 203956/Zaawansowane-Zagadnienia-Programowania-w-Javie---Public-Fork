package pl.mjbladaj.zaaw_java.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mjbladaj.zaaw_java.server.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
