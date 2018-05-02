package pl.mjbladaj.zaaw_java.server.dao.impl;

import org.springframework.stereotype.Component;
import pl.mjbladaj.zaaw_java.server.dao.Dao;

@Component
public class DaoImpl implements Dao {

    @Override
    public String getGreetings() {
        return "Hello World!";
    }
}
