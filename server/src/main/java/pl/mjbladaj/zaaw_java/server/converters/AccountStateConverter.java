package pl.mjbladaj.zaaw_java.server.converters;


import pl.mjbladaj.zaaw_java.server.dto.AccountStateDto;
import pl.mjbladaj.zaaw_java.server.entity.AccountState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AccountStateConverter {
    public static AccountStateDto getAccoutState(AccountState accountState) {
        return
                AccountStateDto
                        .builder()
                        .symbol(accountState.getAvailableCurrency().getSymbol())
                        .amount(accountState.getAmount())
                        .build();
    }

    public static Map<String, Double> getAccountStateDto(List<AccountState> accountStates) {
        return accountStates.stream().collect(HashMap::new,
                (r,s) -> r.put(s.getAvailableCurrency().getSymbol(),s.getAmount()), HashMap::putAll);
    }
}
