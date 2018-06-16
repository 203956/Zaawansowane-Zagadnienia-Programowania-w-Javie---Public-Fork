package pl.mjbladaj.zaaw_java.server.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.mjbladaj.zaaw_java.server.JsonUtils;
import pl.mjbladaj.zaaw_java.server.dto.UserRegistrationData;
import pl.mjbladaj.zaaw_java.server.exceptions.InvalidCredentialsException;
import pl.mjbladaj.zaaw_java.server.service.AccountRegistrationService;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationRestController.class)
@WithMockUser
public class RegistrationRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountRegistrationService accountRegistrationService;

    private UserRegistrationData getUserData(String login, String password, String mail) {
        return UserRegistrationData
                .builder()
                .username(login)
                .password(password)
                .mail(mail)
                .build();
    }

    @Before
    public void setUp() throws Exception {
        UserRegistrationData validUser =
                getUserData("login", "pass", "mail");

        UserRegistrationData validUserAfterRegistration =
                getUserData("login", "pass", "mail");
        validUserAfterRegistration.setId(1);
        validUserAfterRegistration.setPassword("encoded");

        Mockito.when(accountRegistrationService
                .register(validUser))
                .thenReturn(validUserAfterRegistration);

        Mockito.when(accountRegistrationService
                .register(getUserData("a", "pass", "mail")))
                .thenThrow(new InvalidCredentialsException("Username is too short."));

        Mockito.when(accountRegistrationService
                .register(getUserData("login", "a", "mail")))
                .thenThrow(new InvalidCredentialsException("Password is too short."));

        Mockito.when(accountRegistrationService
                .register(getUserData("a", "a", "mail")))
                .thenThrow(new InvalidCredentialsException("Username is too short."));

        Mockito.when(accountRegistrationService
                .register(getUserData("exist", "pass", "mail")))
                .thenThrow(new InvalidCredentialsException("Username is occupied."));
    }

    @After
    public void tearDown() throws Exception {
        Mockito.clearInvocations();
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        mvc.perform(post("/api/public/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getUserData("login", "pass", "mail"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.password", is("encoded")))
                .andExpect(header().exists("Authorization"));
    }
    @Test
    public void shouldReturn401WhenUsernameIsOccupied() throws Exception {
        mvc.perform(post("/api/public/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getUserData("exist", "pass", "mail"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("Username is occupied.")))
                .andExpect(header().doesNotExist("Authorization"));
    }
    @Test
    public void shouldReturn401WhenUsernameIsTooShort() throws Exception {
        mvc.perform(post("/api/public/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getUserData("a", "pass", "mail"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("Username is too short.")))
                .andExpect(header().doesNotExist("Authorization"));
    }
    @Test
    public void shouldReturn401WhenPasswordIsToShort() throws Exception {
        mvc.perform(post("/api/public/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getUserData("login", "a", "mail"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("Password is too short.")))
                .andExpect(header().doesNotExist("Authorization"));
    }
    @Test
    public void shouldReturn401WhenUsernameAndPasswordAreTooShort() throws Exception {
        mvc.perform(post("/api/public/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getUserData("a", "a", "mail"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("Username is too short.")))
                .andExpect(header().doesNotExist("Authorization"));
    }
}