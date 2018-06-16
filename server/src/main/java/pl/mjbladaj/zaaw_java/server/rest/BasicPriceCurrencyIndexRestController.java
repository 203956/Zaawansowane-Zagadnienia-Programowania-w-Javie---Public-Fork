package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.dto.Index;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.impl.BasicPriceCurrencyIndexServiceImpl;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/currencies")
@Api(value = "Basic price currency index for given currency",
        basePath = "/api/public/index/basic",
        produces = "application/json",
        description = "Basic price currency index for selected currency")
    public class BasicPriceCurrencyIndexRestController {

     @Autowired
     private BasicPriceCurrencyIndexServiceImpl basicPriceCurrencyIndexService;

    @ApiOperation(value = "Returns basic price currency index for given currency.",
            response = Index.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currency found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currency not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })

    //@GetMapping("/{currencySymbol}/{startDate}/{endDate}")
  //  @RequestMapping(value = "/country/employees",
   //         method = RequestMethod.GET)
    @RequestMapping(method = GET, path = "/{currencySymbol}/{startDate}/{endDate}")
    @ResponseBody
    public ResponseEntity getBasicPriceCurrencyForGivenCurrency(
            @ApiParam(value = "Symbol of given currency")     @PathVariable("currencySymbol") String currencySymbol,
            @ApiParam(value= "Start Date")                    @PathVariable("startDate") String startDate,
            @ApiParam(value= "End Date")                      @PathVariable("endDate") String endDate)
    {
        try {
            return ResponseEntity.ok(basicPriceCurrencyIndexService.getBasicPriceCurrencyForGivenCurrencyInPeriod(currencySymbol, startDate, endDate));
        }  catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
