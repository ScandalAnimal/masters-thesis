package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import java.util.Arrays;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OptimizeRequest implements Serializable {

    private TeamPlayer[] team;
    private Long transfers;
    private String technique;
    private Long gameWeeks;
    private Long tips;

    public TeamPlayer[] getTeam() {
        return team;
    }

    public void setTeam(TeamPlayer[] team) {
        this.team = team;
    }

    public Long getTransfers() {
        return transfers;
    }

    public void setTransfers(Long transfers) {
        this.transfers = transfers;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public Long getGameWeeks() {
        return gameWeeks;
    }

    public void setGameWeeks(Long gameWeeks) {
        this.gameWeeks = gameWeeks;
    }

    public Long getTips() {
        return tips;
    }

    public void setTips(Long tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "OptimizeRequest{" +
                "team=" + Arrays.toString(team) +
                ", transfers=" + transfers +
                ", technique='" + technique + '\'' +
                ", gameWeeks=" + gameWeeks +
                ", tips=" + tips +
                '}';
    }
}
