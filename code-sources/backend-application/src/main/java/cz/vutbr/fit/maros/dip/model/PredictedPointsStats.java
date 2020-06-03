package cz.vutbr.fit.maros.dip.model;

public class PredictedPointsStats {

    private String playerName;
    private Double predictedPoints;

    public PredictedPointsStats(String playerName, Double predictedPoints) {
        this.playerName = playerName;
        this.predictedPoints = predictedPoints;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Double getPredictedPoints() {
        return predictedPoints;
    }

    public void setPredictedPoints(Double predictedPoints) {
        this.predictedPoints = predictedPoints;
    }

    @Override
    public String toString() {
        return "PredictedPointsStats{" +
                "playerName='" + playerName + '\'' +
                ", predictedPoints=" + predictedPoints +
                '}';
    }
}
