package pl.mjbladaj.zaaw_java.server.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.dto.Rate;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import org.springframework.core.env.Environment;



@Service
public class SelectedCurrencyRateDaoImpl implements SelectedCurrencyRateDao {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Override
    public Rate getRate(String fromCurrency, String toCurrency) {
        ResponseEntity<Rate> response = restTemplate
                .getForEntity(env.getProperty("exchange.currency.base.url") + fromCurrency + "_" + toCurrency, Rate.class);
        return response.getBody();
    }
}
