package pl.mjbladaj.zaaw_java.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mjbladaj.zaaw_java.server.service.Service;



@RestController
public class HomeController {
    @Autowired
    private Service service;

    @RequestMapping("/")
    public ResponseEntity index() {
        return ResponseEntity
                .ok(service.getGreetings());
    }
}
