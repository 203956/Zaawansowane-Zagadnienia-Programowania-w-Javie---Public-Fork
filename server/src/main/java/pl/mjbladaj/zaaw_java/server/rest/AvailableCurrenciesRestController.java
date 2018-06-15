package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.dto.Availability;
import pl.mjbladaj.zaaw_java.server.dto.AvailableCurrencyDto;
import pl.mjbladaj.zaaw_java.server.service.AvailableCurrenciesService;


@RestController
@RequestMapping("/api/public/currencies/available")
@Api(value = "Selected currency rate",
        basePath = "/api/public/currencies/available",
        produces = "application/json",
        description = "Available currencies.")
public class AvailableCurrenciesRestController {

    @Autowired
    private AvailableCurrenciesService availableCurrenciesService;

    @ApiOperation(value = "Returns list of available currencies.",
            response = AvailableCurrencyDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies availability status found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies availability status not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity
                .ok(availableCurrenciesService
                        .getAll());
    }
    @ApiOperation(value = "Returns availability status for currency.",
            response = Availability.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Availability status found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Availability status not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("/{symbol}")
    public ResponseEntity isAvailable(@PathVariable String symbol) {
        return ResponseEntity
                .ok(availableCurrenciesService
                        .isAvailable(symbol));
    }
}
