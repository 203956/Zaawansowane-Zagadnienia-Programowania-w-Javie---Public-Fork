package pl.mjbladaj.zaaw_java.server.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.dto.AvailableCurrencyDto;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.service.AccountService;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

@RestController
@RequestMapping("/api/public/accounts")
@Api(value = "Add currency amount to account.",
        basePath = "/api/public/accounts",
        produces = "application/json",
        description = "Add currency amount to account.")
public class AccountsRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountStateService accountStateService;

    @ApiOperation(value = "Add currency amount to account.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account exists."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Account  doesn't exist."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @RequestMapping(value = "/{login}/{amount}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateAccount(@PathVariable("login") String login, @PathVariable("amount") Double amount, @RequestBody AccountState accountState) {

        Integer accountId = accountService.getAccountId(login);
        accountStateService.updateAccountState(accountId, amount);
        return ResponseEntity.noContent().build();
    }
}
