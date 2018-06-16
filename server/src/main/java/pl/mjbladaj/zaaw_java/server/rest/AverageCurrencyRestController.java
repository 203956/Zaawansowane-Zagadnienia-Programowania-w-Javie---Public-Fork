package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.dto.RateInWeek;
import pl.mjbladaj.zaaw_java.server.dto.UniversalCurrencyRateInTime;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AverageCurrencyRateService;

@RestController
@RequestMapping("/api/public/currencies/average")
@Api(value = "Average currency for given period",
        basePath = "/api/public/currencies/average",
        produces = "application/json",
        description = "Average currency for given period")
public class AverageCurrencyRestController {

    @Autowired
    private AverageCurrencyRateService averageCurrencyRateService;

    @ApiOperation(value = "Returns average currency for given period - by day of week",
            response = RateInWeek.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("/{base}/{goalCurrency}/{startDate}/{endDate}")
    public ResponseEntity getAverageCurrencyForGivenPeriodByDayOfWeek(@ApiParam(value = "base")                     @PathVariable("base") String base,
                                                                      @ApiParam(value = "goalCurrency")     @PathVariable("goalCurrency") String goalCurrency,
                                                                      @ApiParam(value = "start date (yyyy-mm-dd)")   @PathVariable("startDate") String startDate,
                                                                      @ApiParam(value = "end date (yyyy-mm-dd)")     @PathVariable("endDate") String endDate) {
       try {
           return ResponseEntity.ok(averageCurrencyRateService.getAverageCurrencyRateInWeekForGivenPeriod(base, goalCurrency, startDate, endDate));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
