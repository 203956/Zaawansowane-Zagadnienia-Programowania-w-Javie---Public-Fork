package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.dto.*;
import pl.mjbladaj.zaaw_java.server.exceptions.EntityNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.TimePeriodNotAvailableException;
import pl.mjbladaj.zaaw_java.server.service.AmountAdvancedIndexService;
import pl.mjbladaj.zaaw_java.server.service.PriceAdvancedIndexService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/public/index/advanced")
@Api(value = "Selected advanced index for given currency and dates",
        basePath = "/api/public/index/advanced",
        produces = "application/json",
        description = "Advanced index for given currency and dates")
public class AdvancedIndexRestController {

    @Autowired
    private AmountAdvancedIndexService amountAdvancedIndexService;

    @Autowired
    private PriceAdvancedIndexService priceAdvancedIndexService;

    @ApiOperation(value = "Returns advanced amount Paasche index.",
            response = Index.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @RequestMapping(method = POST, path = "/{symbol}/{kindOfIndex}/amount/Paasche")
    public ResponseEntity getAdvanceAmountIndexPaasche(
            @ApiParam(value= "Symbol of currency")               @PathVariable("symbol") String symbol,
            @RequestBody InformationAboutQuantities informationAboutQuantities)
    {
        try {
            List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List< UniversalCurrencyRateInTime > firstListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List<Double> amountsInFirstDateTime = informationAboutQuantities.getAmounts().stream().map(x->x.getAmountInStartDate()).collect(Collectors.toList());
            List<Double> amountsInSecondDateTime = informationAboutQuantities.getAmounts().stream().map(x->x.getAmountInEndDate()).collect(Collectors.toList());


            return ResponseEntity.ok(amountAdvancedIndexService.getCurrencyAmountAdvancedIndex(symbol,informationAboutQuantities.getStartTime(), informationAboutQuantities.getEndTime(), firstListOfCurrencyPrices,
                    secondListOfCurrencyPrices ,amountsInSecondDateTime,amountsInFirstDateTime,
                    KindOfIndex.Paasche));
        }  catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @ApiOperation(value = "Returns advanced price Paasche index.",
            response = Index.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @RequestMapping(method = POST, path = "/{symbol}/price/Paasche")
    public ResponseEntity getAdvancePriceIndexPaasche(
            @ApiParam(value= "Symbol of currency")               @PathVariable("symbol") String symbol,
            @RequestBody InformationAboutQuantities informationAboutQuantities)
    {
        try {
            List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List< UniversalCurrencyRateInTime > firstListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List<Double> amountsInSecondDateTime = informationAboutQuantities.getAmounts().stream().map(x->x.getAmountInEndDate()).collect(Collectors.toList());


            return ResponseEntity.ok(priceAdvancedIndexService.getCurrencyPriceAmountAdvancedIndex(symbol,informationAboutQuantities.getStartTime(), informationAboutQuantities.getEndTime(), firstListOfCurrencyPrices,
                    secondListOfCurrencyPrices ,amountsInSecondDateTime,amountsInSecondDateTime
                    ));
        }  catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    @ApiOperation(value = "Returns advanced amount Laspeyres index.",
            response = Index.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @RequestMapping(method = POST, path = "/{symbol}/{kindOfIndex}/amount/Laspeyres")
    public ResponseEntity getAdvanceAmountIndexLaspeyres(
            @ApiParam(value= "Symbol of currency")               @PathVariable("symbol") String symbol,
            @RequestBody InformationAboutQuantities informationAboutQuantities)
    {
        try {
            List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List< UniversalCurrencyRateInTime > firstListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List<Double> amountsInFirstDateTime = informationAboutQuantities.getAmounts().stream().map(x->x.getAmountInStartDate()).collect(Collectors.toList());
            List<Double> amountsInSecondDateTime = informationAboutQuantities.getAmounts().stream().map(x->x.getAmountInEndDate()).collect(Collectors.toList());


            return ResponseEntity.ok(amountAdvancedIndexService.getCurrencyAmountAdvancedIndex(symbol,informationAboutQuantities.getStartTime(), informationAboutQuantities.getEndTime(), firstListOfCurrencyPrices,
                    secondListOfCurrencyPrices ,amountsInSecondDateTime,amountsInFirstDateTime,
                    KindOfIndex.Laspeyres));
        }  catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @ApiOperation(value = "Returns advanced price Paasche index.",
            response = Index.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Currencies found."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Currencies not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @RequestMapping(method = POST, path = "/{symbol}/price/Laspeyres")
    public ResponseEntity getAdvancePriceIndexLaspeyres(
            @ApiParam(value= "Symbol of currency")               @PathVariable("symbol") String symbol,
            @RequestBody InformationAboutQuantities informationAboutQuantities)
    {
        try {
            List<UniversalCurrencyRateInTime> secondListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List< UniversalCurrencyRateInTime > firstListOfCurrencyPrices = new ArrayList<UniversalCurrencyRateInTime>();
            List<Double> amountsInFirstDateTime = informationAboutQuantities.getAmounts().stream().map(x->x.getAmountInStartDate()).collect(Collectors.toList());


            return ResponseEntity.ok(priceAdvancedIndexService.getCurrencyPriceAmountAdvancedIndex(symbol,informationAboutQuantities.getStartTime(), informationAboutQuantities.getEndTime(), firstListOfCurrencyPrices,
                    secondListOfCurrencyPrices ,amountsInFirstDateTime,amountsInFirstDateTime
            ));
        }  catch (EntityNotFoundException | TimePeriodNotAvailableException e) {
            return ResponseEntity.status(404).build();
        }catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
