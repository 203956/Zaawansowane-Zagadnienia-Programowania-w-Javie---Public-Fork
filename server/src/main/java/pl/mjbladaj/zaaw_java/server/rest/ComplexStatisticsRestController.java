package pl.mjbladaj.zaaw_java.server.rest;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.dto.AverageAndDeviations;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AverageCurrencyRateService;
import pl.mjbladaj.zaaw_java.server.service.impl.ComplexStatisticsServiceImpl;

@RestController
@RequestMapping("/api/public/statistics")
@Api(value = "Statistics for given period ",
        basePath = "/api/public/statistics",
        produces = "application/json",
        description = "Statistics for given period ")
public class ComplexStatisticsRestController {

    @Autowired
    private ComplexStatisticsServiceImpl complexStatisticsService;

    @ApiOperation(value = "Returns average for given period and deviations of each day",
            response = AverageAndDeviations.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping("/{base}/{goalCurrency}/{startDate}/{endDate}")
    public ResponseEntity getAverageAndDeviationsForGivenPeriod(@ApiParam(value = "base")                     @PathVariable("base") String base,
                                                                @ApiParam(value = "goalCurrency")             @PathVariable("goalCurrency") String goalCurrency,
                                                                @ApiParam(value = "start date (yyyy-mm-dd)")  @PathVariable("startDate") String startDate,
                                                                @ApiParam(value = "end date (yyyy-mm-dd)")    @PathVariable("endDate") String endDate) {
        try {
            return ResponseEntity.ok(complexStatisticsService.getAverageAndDeviations(base, goalCurrency, startDate, endDate));
        } catch (EntityNotFoundException | TimePeriodNotAvailableException | CurrencyNotAvailableException e ) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
