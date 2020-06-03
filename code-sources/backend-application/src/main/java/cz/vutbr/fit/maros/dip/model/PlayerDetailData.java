package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlayerDetailData implements Serializable {

    private String season;
    private Long gwIndex;
    private String playerName;
    private Long assists;
    private Long bps;
    private Long goalsScored;
    private Long minutes;
    private Long opponentTeam;
    private Long cleanSheets;
    private Long goalsConceded;
    private Long ownGoals;
    private Long redCards;
    private Long saves;
    private Long totalPoints;
    private Long yellowCards;


    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Long getGwIndex() {
        return gwIndex;
    }

    public void setGwIndex(Long gwIndex) {
        this.gwIndex = gwIndex;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getAssists() {
        return assists;
    }

    public void setAssists(Long assists) {
        this.assists = assists;
    }

    public Long getBps() {
        return bps;
    }

    public void setBps(Long bps) {
        this.bps = bps;
    }

    public Long getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(Long goalsScored) {
        this.goalsScored = goalsScored;
    }

    public Long getMinutes() {
        return minutes;
    }

    public void setMinutes(Long minutes) {
        this.minutes = minutes;
    }

    public Long getOpponentTeam() {
        return opponentTeam;
    }

    public void setOpponentTeam(Long opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

    public Long getCleanSheets() {
        return cleanSheets;
    }

    public void setCleanSheets(Long cleanSheets) {
        this.cleanSheets = cleanSheets;
    }

    public Long getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(Long goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public Long getOwnGoals() {
        return ownGoals;
    }

    public void setOwnGoals(Long ownGoals) {
        this.ownGoals = ownGoals;
    }

    public Long getRedCards() {
        return redCards;
    }

    public void setRedCards(Long redCards) {
        this.redCards = redCards;
    }

    public Long getSaves() {
        return saves;
    }

    public void setSaves(Long saves) {
        this.saves = saves;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Long getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Long yellowCards) {
        this.yellowCards = yellowCards;
    }

    @Override
    public String toString() {
        return "PlayerDetailData{" +
                "season='" + season + '\'' +
                ", gwIndex=" + gwIndex +
                ", playerName='" + playerName + '\'' +
                ", assists=" + assists +
                ", bps=" + bps +
                ", goalsScored=" + goalsScored +
                ", minutes=" + minutes +
                ", opponentTeam=" + opponentTeam +
                ", cleanSheets=" + cleanSheets +
                ", goalsConceded=" + goalsConceded +
                ", ownGoals=" + ownGoals +
                ", redCards=" + redCards +
                ", saves=" + saves +
                ", totalPoints=" + totalPoints +
                ", yellowCards=" + yellowCards +
                '}';
    }
}
