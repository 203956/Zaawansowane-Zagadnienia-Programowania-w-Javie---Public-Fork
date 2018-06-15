package pl.mjbladaj.zaaw_java.server.rest;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjbladaj.zaaw_java.server.dto.ApiError;
import pl.mjbladaj.zaaw_java.server.dto.UserRegistrationData;
import pl.mjbladaj.zaaw_java.server.exceptions.InvalidCredentialsException;
import pl.mjbladaj.zaaw_java.server.exceptions.UsernameOccupiedException;
import pl.mjbladaj.zaaw_java.server.service.AccountRegistrationService;

@RestController
@RequestMapping("/api/public/register")
@Api(value = "Registration",
        basePath = "/api/public/register",
        produces = "application/json",
        description = "Register new user")
public class RegistrationRestController {

    @Autowired
    private AccountRegistrationService accountRegistrationService;

    @ApiOperation(value = "Register new user",
            response = UserRegistrationData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account created"),
            @ApiResponse(code = 401, message = "Username taken."),
            @ApiResponse(code = 403, message = "You are forbidden to access this resource."),
            @ApiResponse(code = 404, message = "Resource not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @PostMapping("")
    public ResponseEntity register(
            @RequestBody UserRegistrationData userRegistrationData
            ) {
        try {
            return ResponseEntity.ok(
            accountRegistrationService
                    .register(userRegistrationData));
        } catch (UsernameOccupiedException | InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(new ApiError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiError(e.getMessage()));
        }
    }
}
