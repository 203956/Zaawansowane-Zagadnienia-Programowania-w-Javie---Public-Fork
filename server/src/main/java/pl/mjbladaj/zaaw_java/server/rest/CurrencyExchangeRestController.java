package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.dto.ApiError;
import pl.mjbladaj.zaaw_java.server.dto.AvailableCurrencyDto;
import pl.mjbladaj.zaaw_java.server.dto.ExchangeStatus;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountStateException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.secruity.TokenAuthenticationUtils;
import pl.mjbladaj.zaaw_java.server.service.CurrencyExchangeService;

@RestController
@RequestMapping("/api/account/exchange/")
@Api(value = "Currency exchange.",
        basePath = "/api/account/exchange/",
        produces = "application/json",
        description = "Currency exchange.")
public class CurrencyExchangeRestController {
    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @ApiOperation(value = "Returns list of available currencies.",
    response = ExchangeStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies availability status found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies availability status not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("{fromCurrency}/{toCurrency}/{amount}")
    public ResponseEntity getAll(
            @RequestHeader("Authorization") String username,
            @PathVariable("fromCurrency") String from,
            @PathVariable("toCurrency") String to,
            @PathVariable("amount") double amount
    ) {
        try {
            return ResponseEntity
                    .ok(currencyExchangeService
                            .exchange(TokenAuthenticationUtils.getUserLogin(username),
                            from, to, amount));
        } catch (CurrencyNotAvailableException | EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiError(e.getMessage()));
        } catch (AccountStateException e) {
            return ResponseEntity.status(409).body(new ApiError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiError(e.getMessage()));
        }
    }


}
