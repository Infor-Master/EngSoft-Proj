package edu.ufp.esof.order.controllers;

import edu.ufp.esof.order.services.authentication.LoginService;

import edu.ufp.esof.order.services.authentication.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/login")
@Controller
public class LoginController {


    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody Credentials credentials){
        Optional<String> optionalHash=this.loginService.generateToken(credentials);

        if(optionalHash.isPresent()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Authorization",
                    optionalHash.get());

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .build();
        }
        return ResponseEntity.badRequest().build();
    }

}
