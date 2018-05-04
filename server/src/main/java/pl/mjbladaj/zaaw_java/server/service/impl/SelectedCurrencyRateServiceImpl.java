package pl.mjbladaj.zaaw_java.server.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mjbladaj.zaaw_java.server.dto.Rate;
import pl.mjbladaj.zaaw_java.server.service.SelectedCurrencyRateService;

@Service
public class SelectedCurrencyRateServiceImpl implements SelectedCurrencyRateService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Rate getRate(String currency) {
        String ROOT_URI = "http://apilayer.net/api/live?access_key=8f0fa1b20a8cefaa04609289fc791cdb&currencies=" + currency + "&format=1";
        ResponseEntity<Rate> response = restTemplate.getForEntity(ROOT_URI, Rate.class);
        return response.getBody();
    }
}
