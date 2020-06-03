package cz.vutbr.fit.maros.dip.service.impl;

import cz.vutbr.fit.maros.dip.exception.CustomException;
import cz.vutbr.fit.maros.dip.model.LoginFormData;
import cz.vutbr.fit.maros.dip.model.MyTeam;
import cz.vutbr.fit.maros.dip.model.UserData;
import cz.vutbr.fit.maros.dip.service.LoginService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class LoginServiceImpl implements LoginService {

    public UserData login(LoginFormData loginFormData) {

        Session session = Requests.session();
        URL url;
        try {
            url = new URL("https://users.premierleague.com/accounts/login/");

            Map<String, Object> params = new HashMap<>();
            params.put("login", loginFormData.getLogin());
            params.put("password", loginFormData.getPassword());
            params.put("app", "plfpl-web");
            params.put("redirect_uri", "https://fantasy.premierleague.com/a/login");
            session.post(url).body(params).send().readToText();

            url = new URL("https://fantasy.premierleague.com/api/me/");
            String resp2 = session.get(url).send().readToText();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(resp2);

            JSONObject player = (JSONObject) json.get("player");
            player.isEmpty();
            Long teamId = (Long) ((JSONObject) json.get("player")).get("entry");

            url = new URL("https://fantasy.premierleague.com/api/my-team/" + teamId.toString());
            MyTeam resp3 = session.get(url).send().readToJson(MyTeam.class);

            UserData userData = new UserData();
            userData.setTeamId(teamId);
            userData.setMyTeam(resp3);

            return userData;

        } catch (MalformedURLException | ParseException e) {
            throw new CustomException("Couldn't login user " + loginFormData.getLogin() + ".");
        }
    }
}
