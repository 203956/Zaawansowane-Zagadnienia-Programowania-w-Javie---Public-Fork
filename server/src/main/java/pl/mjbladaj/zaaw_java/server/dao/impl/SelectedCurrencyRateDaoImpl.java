package pl.mjbladaj.zaaw_java.server.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.converters.UniversalRateConverter;
import pl.mjbladaj.zaaw_java.server.dao.impl.models.Rate;
import pl.mjbladaj.zaaw_java.server.dao.SelectedCurrencyRateDao;
import org.springframework.core.env.Environment;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.models.UniversalRate;

@Service
public class SelectedCurrencyRateDaoImpl implements SelectedCurrencyRateDao {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Override
    public UniversalRate getRate(String fromCurrency, String toCurrency) throws EntityNotFoundException {
        ResponseEntity<Rate> response = restTemplate
                .getForEntity( getUrl(fromCurrency, toCurrency), Rate.class);
        Rate rate = response.getBody();
        return UniversalRateConverter.getCurrencyRate(rate, fromCurrency, toCurrency);
    }

    private String getUrl(String fromCurrency, String toCurrency) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(env.getProperty("exchange.currency.base.url"));
        stringBuilder.append(fromCurrency);
        stringBuilder.append("_");
        stringBuilder.append(toCurrency);

        return stringBuilder.toString();
    }
}
