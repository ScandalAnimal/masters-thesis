package cz.vutbr.fit.maros.dip.service.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.vutbr.fit.maros.dip.constants.ApiConstants;
import cz.vutbr.fit.maros.dip.exception.CustomException;
import cz.vutbr.fit.maros.dip.model.Team;
import cz.vutbr.fit.maros.dip.service.TeamService;
import cz.vutbr.fit.maros.dip.util.DatasetUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class TeamServiceImpl implements TeamService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamServiceImpl.class);

    public List<Team> getAllTeams() {

        LOG.info("Started getting all teams.");
        List<Team> teams = new ArrayList<>();
        String fileName = ApiConstants.BASE_URL + "teams.csv";

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            String[] keys = header.split(",");
            String line;
            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < keys.length; i++) {
                    jsonObject.put(keys[i], values[i]);
                }

                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                Team team = gson.fromJson(jsonObject.toString(), Team.class);
                teams.add(team);
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        LOG.info("Finished getting all teams.");
        return teams;
    }

    public Integer getTeamByPlayerName(String firstName, String lastName) {

        String fileName = ApiConstants.BASE_URL + "players_raw.csv";

        String[] keys = {"first_name", "second_name", "team" };
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
            String line;
            while ((line = br.readLine()) != null) {

                String filteredLine = DatasetUtils.filterLine(line, indexes);
                String[] values = filteredLine.split(",");
                if (Objects.equals(values[0], firstName) && Objects.equals(values[1], lastName)) {
                    return Integer.parseInt(values[2]);
                }
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        return null;
    }
}
