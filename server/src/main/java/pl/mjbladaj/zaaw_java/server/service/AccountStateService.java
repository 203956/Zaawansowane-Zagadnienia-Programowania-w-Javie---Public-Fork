package pl.mjbladaj.zaaw_java.server.service;

import java.util.Map;

public interface AccountStateService {
    Map<String, Double> getAllUserAccountState(Integer accountId);
}
