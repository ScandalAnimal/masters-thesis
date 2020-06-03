package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Player implements Serializable {

    private Long bonus;
    private Long totalPoints;
    private Double influence;
    private Long nowCost;
    private Double creativity;
    private Long goalsScored;
    private Long minutes;
    private Long yellowCards;
    private Double selectedByPercent;
    private String secondName;
    private Long cleanSheets;
    private Long goalsConceded;
    private Long redCards;
    private Long assists;
    private String firstName;
    private Long bps;
    private Double threat;
    private Double ictIndex;
    private String team;
    private Long teamCode;
    private Long elementType;

    public Long getElementType() {
        return elementType;
    }

    public void setElementType(Long elementType) {
        this.elementType = elementType;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Long getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Long teamCode) {
        this.teamCode = teamCode;
    }

    public Long getBonus() {
        return bonus;
    }

    public void setBonus(Long bonus) {
        this.bonus = bonus;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Double getInfluence() {
        return influence;
    }

    public void setInfluence(Double influence) {
        this.influence = influence;
    }

    public Long getNowCost() {
        return nowCost;
    }

    public void setNowCost(Long nowCost) {
        this.nowCost = nowCost;
    }

    public Double getCreativity() {
        return creativity;
    }

    public void setCreativity(Double creativity) {
        this.creativity = creativity;
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

    public Long getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Long yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Double getSelectedByPercent() {
        return selectedByPercent;
    }

    public void setSelectedByPercent(Double selectedByPercent) {
        this.selectedByPercent = selectedByPercent;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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

    public Long getRedCards() {
        return redCards;
    }

    public void setRedCards(Long redCards) {
        this.redCards = redCards;
    }

    public Long getAssists() {
        return assists;
    }

    public void setAssists(Long assists) {
        this.assists = assists;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getBps() {
        return bps;
    }

    public void setBps(Long bps) {
        this.bps = bps;
    }

    public Double getThreat() {
        return threat;
    }

    public void setThreat(Double threat) {
        this.threat = threat;
    }

    public Double getIctIndex() {
        return ictIndex;
    }

    public void setIctIndex(Double ictIndex) {
        this.ictIndex = ictIndex;
    }

    @Override
    public String toString() {
        return "Player{" +
                "bonus=" + bonus +
                ", totalPoints=" + totalPoints +
                ", influence=" + influence +
                ", nowCost=" + nowCost +
                ", creativity=" + creativity +
                ", goalsScored=" + goalsScored +
                ", minutes=" + minutes +
                ", yellowCards=" + yellowCards +
                ", selectedByPercent=" + selectedByPercent +
                ", secondName='" + secondName + '\'' +
                ", cleanSheets=" + cleanSheets +
                ", goalsConceded=" + goalsConceded +
                ", redCards=" + redCards +
                ", assists=" + assists +
                ", firstName='" + firstName + '\'' +
                ", bps=" + bps +
                ", threat=" + threat +
                ", ictIndex=" + ictIndex +
                ", team='" + team + '\'' +
                ", teamCode=" + teamCode +
                ", elementType=" + elementType +
                '}';
    }
}
