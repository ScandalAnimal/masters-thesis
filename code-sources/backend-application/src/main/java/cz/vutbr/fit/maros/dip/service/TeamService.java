package cz.vutbr.fit.maros.dip.service;

import cz.vutbr.fit.maros.dip.model.Team;
import java.util.List;

public interface TeamService {

    List<Team> getAllTeams();

    Integer getTeamByPlayerName(String firstName, String lastName);
}
