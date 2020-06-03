package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlayerInjuryData implements Serializable {

    private String chanceOfPlayingNextRound;
    private String chanceOfPlayingThisRound;
    private Long teamCode;
    private String firstName;
    private String news;
    private String newsAdded;
    private String secondName;
    private Long yellowCards;
    private Long redCards;
    private String webName;

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getChanceOfPlayingNextRound() {
        return chanceOfPlayingNextRound;
    }

    public void setChanceOfPlayingNextRound(String chanceOfPlayingNextRound) {
        this.chanceOfPlayingNextRound = chanceOfPlayingNextRound;
    }

    public String getChanceOfPlayingThisRound() {
        return chanceOfPlayingThisRound;
    }

    public void setChanceOfPlayingThisRound(String chanceOfPlayingThisRound) {
        this.chanceOfPlayingThisRound = chanceOfPlayingThisRound;
    }

    public Long getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Long teamCode) {
        this.teamCode = teamCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getNewsAdded() {
        return newsAdded;
    }

    public void setNewsAdded(String newsAdded) {
        this.newsAdded = newsAdded;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Long getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Long yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Long getRedCards() {
        return redCards;
    }

    public void setRedCards(Long redCards) {
        this.redCards = redCards;
    }


    @Override
    public String toString() {
        return "PlayerInjuryData{" +
                "chanceOfPlayingNextRound='" + chanceOfPlayingNextRound + '\'' +
                ", chanceOfPlayingThisRound='" + chanceOfPlayingThisRound + '\'' +
                ", teamCode=" + teamCode +
                ", firstName='" + firstName + '\'' +
                ", news='" + news + '\'' +
                ", newsAdded='" + newsAdded + '\'' +
                ", secondName='" + secondName + '\'' +
                ", yellowCards=" + yellowCards +
                ", redCards=" + redCards +
                ", webName=" + webName +
                '}';
    }
}
