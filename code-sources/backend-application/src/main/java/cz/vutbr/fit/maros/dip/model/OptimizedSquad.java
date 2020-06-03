package cz.vutbr.fit.maros.dip.model;

import java.io.Serializable;
import java.util.List;

public class OptimizedSquad implements Serializable {

    private List<Long> team;
    private Long captain;
    private Long viceCaptain;

    public List<Long> getTeam() {
        return team;
    }

    public void setTeam(List<Long> team) {
        this.team = team;
    }

    public Long getCaptain() {
        return captain;
    }

    public void setCaptain(Long captain) {
        this.captain = captain;
    }

    public Long getViceCaptain() {
        return viceCaptain;
    }

    public void setViceCaptain(Long viceCaptain) {
        this.viceCaptain = viceCaptain;
    }

    @Override
    public String toString() {
        return "OptimizedSquad{" +
                "team=" + team +
                ", captain=" + captain +
                ", viceCaptain=" + viceCaptain +
                '}';
    }
}
