package pl.mjbladaj.zaaw_java.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.service.RateService;

@RestController
@RequestMapping("/api")
public class SelectedCurrencyRestController {

    @Autowired
    private RateService rateService;

    @GetMapping("/{currency}/rate")
    public ResponseEntity getConvertedRate(@PathVariable String currency) {
        return ResponseEntity
                .ok(rateService
                        .getConvertedRate(currency, "PLN"));
    }
}
