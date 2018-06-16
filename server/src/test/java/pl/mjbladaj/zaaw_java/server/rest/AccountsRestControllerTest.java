package pl.mjbladaj.zaaw_java.server.rest;

import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
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
import pl.mjbladaj.zaaw_java.server.dto.AccountStateData;
import pl.mjbladaj.zaaw_java.server.entity.Account;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;
import pl.mjbladaj.zaaw_java.server.entity.AvailableCurrency;
import pl.mjbladaj.zaaw_java.server.exceptions.CurrencyNotAvailableException;
import pl.mjbladaj.zaaw_java.server.secruity.TokenAuthenticationUtils;
import pl.mjbladaj.zaaw_java.server.service.AccountService;
import pl.mjbladaj.zaaw_java.server.service.AccountStateService;

import java.util.Map;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountsRestController.class)
@WithMockUser
public class AccountsRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountStateService accountStateService;

    @MockBean
    private AccountService accountService;

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
                .id(1)
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

    private AccountState getAccountState() {
        AvailableCurrency currency = AvailableCurrency
                .builder()
                .symbol("PLN")
                .name("Złotówka polska")
                .build();

        return AccountState
                .builder()
                .account(getAccount())
                .amount(3.23)
                .availableCurrency(currency)
                .build();
    }

    @Before
    public void setUp() {
        token = TokenAuthenticationUtils.buildToken("exist");

        given(accountStateService.getAllUserAccountState(getAccount().getId()))
                .willReturn(getAllUserState());
    }

    @After
    public void tearDown() {
        Mockito.clearInvocations(accountStateService, accountService);
        Mockito.reset(accountStateService, accountService);
    }
/*
    @Test
    public void shouldAddMoneyToAccount() throws Exception {
        mvc.perform(post("/api/payment")
                .with(csrf())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJson(
                        getAccountStateData(32.5, "PLN"))))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    public void shouldReturnAllUserStates() throws Exception {
        mvc.perform(get("/api/account/state")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].symbol", is("PLN")))
                .andExpect(jsonPath("$[0].amount", is(3.4)))
                .andExpect(jsonPath("$[1].symbol", is("USD")))
                .andExpect(jsonPath("$[1].amount", is( 5.6)));
    }*/
}