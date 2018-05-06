package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.dto.CurrencyRate;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.service.RateService;


@RestController
@RequestMapping("/api/currencies")
@Api(value = "Selected currency rate",
        basePath = "/api/currencies",
        produces = "application/json",
        description = "Selected currency rate")
public class SelectedCurrencyRestController {

    @Autowired
    private RateService rateService;

    @ApiOperation(value = "Returns selected currency rate.",
            notes = "If currency with given symbol does not exists, 404 will be returned.",
            response = CurrencyRate.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currency founded."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currency not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("/{symbol}/rate")
    public ResponseEntity getConvertedRate(@ApiParam(value = "symbol of selected currency") @PathVariable String symbol) {
        try {
            return ResponseEntity.ok(rateService.getConvertedRate(symbol, "PLN"));
        } catch (EntityNotFoundException | CurrencyNotAvailableException e) {
            System.out.println(e);
            return ResponseEntity.status(404).build();
        }
    }
}
