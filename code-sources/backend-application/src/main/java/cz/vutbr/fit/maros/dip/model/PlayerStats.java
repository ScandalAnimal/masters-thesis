package cz.vutbr.fit.maros.dip.model;

public class PlayerStats {

    private String playerName;
    private Double totalPoints;
    private Double cost;
    private Integer position;
    private Double costPointIndex;
    private Double costPointIndexLast6;
    private Double predictedPoints;

    public PlayerStats(String playerName, Double totalPoints, Double cost, Integer position, Double costPointIndex, Double costPointIndexLast6, Double predictedPoints) {
        this.playerName = playerName;
        this.totalPoints = totalPoints;
        this.cost = cost;
        this.position = position;
        this.costPointIndex = costPointIndex;
        this.costPointIndexLast6 = costPointIndexLast6;
        this.predictedPoints = predictedPoints;
    }

    public Double getPredictedPoints() {
        return predictedPoints;
    }

    public void setPredictedPoints(Double predictedPoints) {
        this.predictedPoints = predictedPoints;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getCostPointIndex() {
        return costPointIndex;
    }

    public void setCostPointIndex(Double costPointIndex) {
        this.costPointIndex = costPointIndex;
    }

    public Double getCostPointIndexLast6() {
        return costPointIndexLast6;
    }

    public void setCostPointIndexLast6(Double costPointIndexLast6) {
        this.costPointIndexLast6 = costPointIndexLast6;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "playerName='" + playerName + '\'' +
                ", totalPoints=" + totalPoints +
                ", cost=" + cost +
                ", position=" + position +
                ", costPointIndex=" + costPointIndex +
                ", costPointIndexLast6=" + costPointIndexLast6 +
                ", predictedPoints=" + predictedPoints +
                '}';
    }
}
