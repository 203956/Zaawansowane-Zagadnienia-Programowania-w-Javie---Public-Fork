package pl.mjbladaj.zaaw_java.server.secruity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

@Api(value = "Authentication",
        basePath = "/api/login",
        description = "Authentication process")
@RequestMapping(value = "/api/login")
@RestController
public class SwaggerLogin {
    /**
     * Implemented by Spring Security
     */
    @ApiOperation(value = "Login", notes = "Login with the given credentials.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "You are logged in."),
            @ApiResponse(code = 401, message = "Credentials invalid."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @RequestMapping(method = RequestMethod.POST)
    void login (@RequestBody User user) {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }
}
