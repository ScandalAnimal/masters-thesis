package cz.vutbr.fit.maros.dip.model;

import java.io.Serializable;


public class LoginFormData implements Serializable {

    private String login;

    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginForm{"
                + "login='" + login + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
