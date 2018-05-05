package pl.mjbladaj.zaaw_java.server.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.dao.CurrencyExchangeRate;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;

@Component
public class CurrencyExchangeImpl implements CurrencyExchangeRate {

    private String API = "https://free.currencyconverterapi.com/api/v5/convert?";
    @Autowired
    private RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyExchangeImpl.class);

    @Override
    public double getActualExchangeRate() {
        ResponseEntity<CurrencyRate> response = restTemplate.getForEntity(API + "q=USD_PHP&compact=y", CurrencyRate.class);
        LOG.warn(response.getBody().toString());
        return 0;
    }
}
