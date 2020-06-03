package cz.vutbr.fit.maros.dip.model;

import java.io.Serializable;

public class Fixture implements Serializable {

    private double gw;
    private boolean finished;
    private int homeTeam;
    private int awayTeam;
    private int homeTeamDifficulty;
    private int awayTeamDifficulty;

    public Fixture(double gw, boolean finished, int homeTeam, int awayTeam, int homeTeamDifficulty, int awayTeamDifficulty) {
        this.gw = gw;
        this.finished = finished;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeTeamDifficulty = homeTeamDifficulty;
        this.awayTeamDifficulty = awayTeamDifficulty;
    }

    public double getGw() {
        return gw;
    }

    public void setGw(double gw) {
        this.gw = gw;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(int homeTeam) {
        this.homeTeam = homeTeam;
    }

    public int getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(int awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeTeamDifficulty() {
        return homeTeamDifficulty;
    }

    public void setHomeTeamDifficulty(int homeTeamDifficulty) {
        this.homeTeamDifficulty = homeTeamDifficulty;
    }

    public int getAwayTeamDifficulty() {
        return awayTeamDifficulty;
    }

    public void setAwayTeamDifficulty(int awayTeamDifficulty) {
        this.awayTeamDifficulty = awayTeamDifficulty;
    }

    @Override
    public String toString() {
        return "Fixture{" +
                "gw=" + gw +
                ", finished=" + finished +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", homeTeamDifficulty=" + homeTeamDifficulty +
                ", awayTeamDifficulty=" + awayTeamDifficulty +
                '}';
    }
}
