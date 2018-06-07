package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateCalculationsService;
import pl.mjbladaj.zaaw_java.server.service.HistoricalRateService;

@RestController
@RequestMapping("/api/currencies")
@Api(value = "Selected currency rate",
        basePath = "/api/currencies",
        produces = "application/json",
        description = "Selected currency rate")
public class HistoricalCurrencyRestController {
    @Autowired
    private HistoricalRateService historicalRateService;

    @Autowired
    private HistoricalRateCalculationsService historicalRateCalculationsService;


    @GetMapping("/{symbol}/{date}/rate")
    public ResponseEntity getConvertedRateForGivenDay(@ApiParam(value = "symbol of selected currency") @PathVariable("symbol") String symbol,
                                                      @PathVariable("date") String date) throws TimePeriodNotAvailableException {
        try {
            return ResponseEntity.ok(historicalRateService.getConvertedRateForGivenDay(symbol, "PLN", date));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{symbol}/{startDate}/{endDate}/rate")
    public ResponseEntity getConvertedRateForGivenPeriod(@ApiParam(value = "symbol of selected currency") @PathVariable("symbol") String symbol,
                                                         @PathVariable("startDate") String startDate,
                                                         @PathVariable("endDate")String endDate) throws TimePeriodNotAvailableException {
        try {
            return ResponseEntity.ok(historicalRateService.getConvertedRateForGivenPeriod(symbol, "PLN", startDate, endDate));
        } catch (EntityNotFoundException
                | CurrencyNotAvailableException  | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("{base}/{symbol}/{startDate}/{endDate}/rate")
    public ResponseEntity getConvertedRateForGivenPeriod(@ApiParam(value = "symbol of selected currency") @PathVariable("base") String base,
                                                         @PathVariable("symbol") String symbol,
                                                         @PathVariable("startDate") String startDate,
                                                         @PathVariable("endDate")String endDate) {
        try {
            return ResponseEntity.ok(historicalRateService.getConvertedRateForGivenPeriod(base, symbol, startDate, endDate));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException
                | CurrencyNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("{base}/{symbol1}/{symbol2}/{startDate}/{endDate}/rate")
    public ResponseEntity getConvertedRateForGivenPeriod(@ApiParam(value = "symbol of selected currency") @PathVariable("base") String base,
                                                         @PathVariable("symbol1") String symbol1,
                                                         @PathVariable("symbol2") String symbol2,
                                                         @PathVariable("startDate") String startDate,
                                                         @PathVariable("endDate")String endDate) {
        try {
            return ResponseEntity.ok(historicalRateCalculationsService.getDifferenceInRatesRatesForGivenPeriod(base, symbol1, symbol2, startDate, endDate));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException
                | CurrencyNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
