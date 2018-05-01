package pl.mjbladaj.zaaw_java.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mjbladaj.zaaw_java.server.dao.Dao;
import pl.mjbladaj.zaaw_java.server.service.Service;

@Component
public class ServiceImpl implements Service {
    @Autowired
    private Dao dao;

    @Override
    public String getGreetings() {
        return dao.getGreetings();
    }
}
