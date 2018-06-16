package pl.mjbladaj.zaaw_java.server.rest;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.dto.AccountStateData;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.exceptions.AccountNotFoundException;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.secruity.TokenAuthenticationUtils;
import pl.mjbladaj.zaaw_java.server.service.AccountService;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

@RestController
@RequestMapping("/api")
@Api(value = "Add currency amount to account.",
        basePath = "/api",
        produces = "application/json",
        description = "Add currency amount to account.")
public class AccountsRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountStateService accountStateService;

    @ApiOperation(value = "Add currency amount to account.",
            response = AccountStateData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account exists."),
            @ApiResponse(code = 401, message = "You are unauthorized."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Account  doesn't exist."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })

    @ApiImplicitParam(name = "Authorization", value = "Authorization token",
            required = true, dataType = "string", paramType = "header")
    @PostMapping("/payment")
    public ResponseEntity updateAccount(
        @RequestBody AccountStateData accountStateData,
        @RequestHeader(TokenAuthenticationUtils.HEADER_STRING) String token)  {
        String login = TokenAuthenticationUtils.getUserLogin(token);

        try {
            Account account = accountService.getAccount(login);
            accountStateService.addMoneyToAccount(
                    account.getLogin(),
                    accountStateData.getSymbol(),
                    accountStateData.getAmount());
            return ResponseEntity.noContent().build();
        } catch (CurrencyNotAvailableException  | AccountNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

        @ApiOperation(value = "Return account state.")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "Account exists."),
                @ApiResponse(code = 401, message = "You are unauthorized."),
                @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
                @ApiResponse(code = 404, message = "Account  doesn't exist."),
                @ApiResponse(code = 500, message = "Unknown error.")
        })
    @RequestMapping(value = "/account/state", method = RequestMethod.GET)
    public ResponseEntity getConvertedAccountState(
            @RequestHeader(TokenAuthenticationUtils.HEADER_STRING) String token) {
        String login = TokenAuthenticationUtils.getUserLogin(token);
            try {
                Account account = accountService.getAccount(login);
                return ResponseEntity.ok(
                        accountStateService.getAllUserAccountState(account.getId()));
            } catch (AccountNotFoundException e) {
                return ResponseEntity.status(404).build();
            }
    }
}
