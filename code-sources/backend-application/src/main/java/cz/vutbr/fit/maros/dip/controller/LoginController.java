package cz.vutbr.fit.maros.dip.controller;

import com.google.gson.Gson;
import cz.vutbr.fit.maros.dip.model.LoginFormData;
import cz.vutbr.fit.maros.dip.model.UserData;
import cz.vutbr.fit.maros.dip.results.ResponseWrapper;
import cz.vutbr.fit.maros.dip.service.impl.LoginServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginServiceImpl loginService;

    public LoginController(LoginServiceImpl loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseWrapper<UserData> login(@RequestBody String json) {
        Gson gson = new Gson();
        LoginFormData loginFormData = gson.fromJson(json, LoginFormData.class);
        UserData userData = loginService.login(loginFormData);
        if (userData == null) {
            return new ResponseWrapper<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseWrapper<>(userData, HttpStatus.OK);
    }

}
