package pl.mjbladaj.zaaw_java.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.service.SelectedCurrencyRateService;

@RestController
@RequestMapping("/api")
public class SelectedCurrencyRestController {

    @Autowired
    private SelectedCurrencyRateService selectedCurrencyRateService;

    @GetMapping("/{currency}/rate")
    public ResponseEntity getRate(@PathVariable String currency) {
        return ResponseEntity
                .ok(selectedCurrencyRateService
                        .getRate(currency));
    }
}
