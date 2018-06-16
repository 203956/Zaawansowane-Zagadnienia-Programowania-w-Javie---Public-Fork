package pl.mjbladaj.zaaw_java.server.rest;

import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.mjbladaj.zaaw_java.server.JsonUtils;
import pl.mjbladaj.zaaw_java.server.dto.AccountStateData;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.secruity.TokenAuthenticationUtils;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountsRestController.class)
@WithMockUser
public class AccountsRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountStateService accountStateService;


    private String token;

    private AccountStateData getAccountStateData(Double amount, String symbol) {
        return AccountStateData
                .builder()
                .amount(amount)
                .symbol(symbol)
                .build();
    }

    private Account getAccount() {
        return Account
                .builder()
                .login("exist")
                .password("{noop}pass")
                .build();
    }

    private Map<String, Double> getAllUserState() {
         return ImmutableMap.<String, Double>builder()
                 .put("PLN", 3.4)
                 .put("USD", 5.6)
                 .build();
    }


    @Before
    public void setUp() throws CurrencyNotAvailableException {
        token = TokenAuthenticationUtils.buildToken("exist");

        Mockito.when(accountStateService.getUserAccountState(getAccount().getLogin()))
                .thenReturn(getAllUserState());

        Mockito.doThrow(new CurrencyNotAvailableException())
                .when(accountStateService)
                .addMoneyToAccount("exist", "MVC", 32.5);
    }

    @After
    public void tearDown() {
        Mockito.clearInvocations(accountStateService);
        Mockito.reset(accountStateService);
    }

    @Test
    public void shouldAddMoneyToAccount() throws Exception {
        mvc.perform(post("/api/account/payment")
                .with(csrf())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getAccountStateData(32.5, "PLN"))))
                .andExpect(status().isNoContent());
    }
    @Test
    public void shouldReturn404WhenCurrencyIsNotAvailable() throws Exception {
        mvc.perform(post("/api/account/payment")
                .with(csrf())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getAccountStateData(32.5, "MVC"))))
                .andExpect(status().is(404));
    }
    @Test
    public void shouldReturnAllUserStates() throws Exception {
        mvc.perform(get("/api/account/state")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.PLN", is(3.4)))
                .andExpect(jsonPath("$.USD", is( 5.6)));
    }
}