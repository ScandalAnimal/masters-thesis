package cz.vutbr.fit.maros.dip.controller;

import cz.vutbr.fit.maros.dip.model.Team;
import cz.vutbr.fit.maros.dip.results.ResponseWrapper;
import cz.vutbr.fit.maros.dip.service.TeamService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping()
    public ResponseWrapper<List<Team>> getAllTeams() {
        return new ResponseWrapper<>(teamService.getAllTeams(), HttpStatus.OK);
    }

}
