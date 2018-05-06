package pl.mjbladaj.zaaw_java.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;


@RestController
@RequestMapping("/api/currencies/available")
public class AvailableCurrenciesRestController {

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity
                .ok(availableCurrenciesService
                        .getAll());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity isAvailable(@PathVariable String symbol) {
        return ResponseEntity
                .ok(availableCurrenciesService
                        .isAvailable(symbol));
    }
}
