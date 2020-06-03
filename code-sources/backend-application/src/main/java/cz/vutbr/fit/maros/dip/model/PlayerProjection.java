package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlayerProjection implements Serializable {

    private String playerName;
    private Long predictedPoints;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getPredictedPoints() {
        return predictedPoints;
    }

    public void setPredictedPoints(Long predictedPoints) {
        this.predictedPoints = predictedPoints;
    }

    public PlayerProjection(String playerName, Long predictedPoints) {
        this.playerName = playerName;
        this.predictedPoints = predictedPoints;
    }

    @Override
    public String toString() {
        return "PlayerProjection{" +
                "playerName='" + playerName + '\'' +
                ", predictedPoints=" + predictedPoints +
                '}';
    }
}
