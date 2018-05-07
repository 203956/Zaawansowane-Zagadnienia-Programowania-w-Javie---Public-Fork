package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;


@RestController
@RequestMapping("/api/currencies/available")
@Api(value = "Selected currency rate",
        basePath = "/api/currencies/available",
        produces = "application/json",
        description = "Available currencies.")
public class AvailableCurrenciesRestController {

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @ApiOperation(value = "Returns list of available currencies.",
            response = AvailableCurrency.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies founded."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
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
