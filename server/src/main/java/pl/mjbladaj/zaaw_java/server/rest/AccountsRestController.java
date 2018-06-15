package pl.mjbladaj.zaaw_java.server.rest;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.secruity.TokenAuthenticationUtils;
import pl.mjbladaj.zaaw_java.server.service.AccountService;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

@RestController
@RequestMapping("/api/payment")
@Api(value = "Add currency amount to account.",
        basePath = "/api/payment",
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

    @ApiImplicitParam(name = "Authorization", value = "Authorization token",
            required = true, dataType = "string", paramType = "header")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateAccount(
        @RequestBody AccountState accountState,
        @RequestHeader(TokenAuthenticationUtils.HEADER_STRING) String token) {
            String login = TokenAuthenticationUtils.getUserLogin(token);
            Integer accountId = accountService.getAccountId(login);
            accountStateService.updateAccountState(accountId);

            return ResponseEntity.noContent().build();
    }
}
