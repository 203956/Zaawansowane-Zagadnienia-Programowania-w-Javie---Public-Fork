package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateCalculationsService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;

@RestController
@RequestMapping("/api/public/currencies")
@Api(value = "Selected currency rate",
        basePath = "/api/public/currencies",
        produces = "application/json",
        description = "Selected currency rate")
public class HistoricalCurrencyRestController {
    @Autowired
    private HistoricalRateService historicalRateService;

    @Autowired
    private HistoricalRateCalculationsService historicalRateCalculationsService;

    @ApiOperation(value = "Returns converted rate to PLN for given day.",
            response = UniversalCurrencyRateInTime.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("/{symbol}/{date}/rate")
    public ResponseEntity getConvertedRateForGivenDay(
            @ApiParam(value = "symbol of selected currency")    @PathVariable("symbol") String symbol,
            @ApiParam(value= "date to get currency date from ") @PathVariable("date") String date) {
        try {
            return ResponseEntity.ok(historicalRateService.getConvertedRateForGivenDay(symbol, "PLN", date));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @ApiOperation(value = "Returns converted rate to PLN for given period.",
            response = UniversalCurrencyRateInTime.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("/{symbol}/{startDate}/{endDate}/rate")
    public ResponseEntity getConvertedRateForGivenPeriod(@ApiParam(value = "symbol of selected currency") @PathVariable("symbol") String symbol,
                                                         @ApiParam(value = "period start date")           @PathVariable("startDate") String startDate,
                                                         @ApiParam(value = "period end date")             @PathVariable("endDate")String endDate)  {
        try {
            return ResponseEntity.ok(historicalRateService.getConvertedRateForGivenPeriod(symbol, "PLN", startDate, endDate));
        } catch (EntityNotFoundException
                | CurrencyNotAvailableException  | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @ApiOperation(value = "Returns converted rate for given currencies and given period.",
            response = UniversalCurrencyRateInTime.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("{base}/{symbol}/{startDate}/{endDate}/rate")
    public ResponseEntity getConvertedRateForGivenPeriod(@ApiParam(value = "symbol of base currency") @PathVariable("base") String base,
                                                         @ApiParam(value = "symbol of goal currency") @PathVariable("symbol") String symbol,
                                                         @ApiParam(value = "period start date")       @PathVariable("startDate") String startDate,
                                                         @ApiParam(value = "period end date")         @PathVariable("endDate")String endDate) {
        try {
            return ResponseEntity.ok(historicalRateService.getConvertedRateForGivenPeriod(base, symbol, startDate, endDate));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException
                | CurrencyNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @ApiOperation(value = "Returns difference in rates for given currencies and given period.",
            response = UniversalCurrencyRateInTime.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("{base}/{symbol1}/{symbol2}/{startDate}/{endDate}/rate")
    public ResponseEntity getConvertedRateForGivenPeriod(@ApiParam(value = "symbol of base currency")        @PathVariable("base") String base,
                                                         @ApiParam(value = "symbol of first goal currency")  @PathVariable("symbol1") String symbol1,
                                                         @ApiParam(value = "symbol of second goal currency") @PathVariable("symbol2") String symbol2,
                                                         @ApiParam(value = "period start date")              @PathVariable("startDate") String startDate,
                                                         @ApiParam(value = "period end date")                @PathVariable("endDate")String endDate) {
        try {
            return ResponseEntity.ok(historicalRateCalculationsService.getDifferenceInRatesRatesForGivenPeriod(base, symbol1, symbol2, startDate, endDate));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException
                | CurrencyNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
