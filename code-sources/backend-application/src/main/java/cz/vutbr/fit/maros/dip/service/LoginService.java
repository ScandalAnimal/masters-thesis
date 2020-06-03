package cz.vutbr.fit.maros.dip.service;

import cz.vutbr.fit.maros.dip.model.LoginFormData;
import cz.vutbr.fit.maros.dip.model.UserData;

public interface LoginService {

    UserData login(LoginFormData loginFormData);

}

