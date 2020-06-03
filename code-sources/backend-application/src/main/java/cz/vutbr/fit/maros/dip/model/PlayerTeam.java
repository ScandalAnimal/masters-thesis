package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlayerTeam implements Serializable {

    private String secondName;
    private String firstName;
    private String team;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    @Override
    public String toString() {
        return "PlayerTeam{" +
                ", secondName='" + secondName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", team='" + team + '\'' +
                '}';
    }
}
