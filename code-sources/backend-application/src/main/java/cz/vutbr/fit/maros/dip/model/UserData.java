package cz.vutbr.fit.maros.dip.model;

import java.io.Serializable;


public class UserData implements Serializable {

    private Long teamId;

    private MyTeam myTeam;

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public MyTeam getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(MyTeam myTeam) {
        this.myTeam = myTeam;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "teamId=" + teamId +
                ", myTeam=" + myTeam +
                '}';
    }
}
