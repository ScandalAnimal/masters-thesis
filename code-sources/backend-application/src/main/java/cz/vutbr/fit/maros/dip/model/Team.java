package cz.vutbr.fit.maros.dip.model;

public class Team {

    private Long code;
    private Long draw;
    private String form;
    private Long id;
    private Long loss;
    private Long played;
    private Long points;
    private Long position;
    private String name;
    private String shortName;
    private String unavailable;
    private Long strength;
    private String teamDivision;
    private Long win;
    private Long strengthOverallHome;
    private Long strengthOverallAway;
    private Long strengthAttackHome;
    private Long strengthAttackAway;
    private Long strengthDefenceHome;
    private Long strengthDefenceAway;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getDraw() {
        return draw;
    }

    public void setDraw(Long draw) {
        this.draw = draw;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoss() {
        return loss;
    }

    public void setLoss(Long loss) {
        this.loss = loss;
    }

    public Long getPlayed() {
        return played;
    }

    public void setPlayed(Long played) {
        this.played = played;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(String unavailable) {
        this.unavailable = unavailable;
    }

    public Long getStrength() {
        return strength;
    }

    public void setStrength(Long strength) {
        this.strength = strength;
    }

    public String getTeamDivision() {
        return teamDivision;
    }

    public void setTeamDivision(String teamDivision) {
        this.teamDivision = teamDivision;
    }

    public Long getWin() {
        return win;
    }

    public void setWin(Long win) {
        this.win = win;
    }

    public Long getStrengthOverallHome() {
        return strengthOverallHome;
    }

    public void setStrengthOverallHome(Long strengthOverallHome) {
        this.strengthOverallHome = strengthOverallHome;
    }

    public Long getStrengthOverallAway() {
        return strengthOverallAway;
    }

    public void setStrengthOverallAway(Long strengthOverallAway) {
        this.strengthOverallAway = strengthOverallAway;
    }

    public Long getStrengthAttackHome() {
        return strengthAttackHome;
    }

    public void setStrengthAttackHome(Long strengthAttackHome) {
        this.strengthAttackHome = strengthAttackHome;
    }

    public Long getStrengthAttackAway() {
        return strengthAttackAway;
    }

    public void setStrengthAttackAway(Long strengthAttackAway) {
        this.strengthAttackAway = strengthAttackAway;
    }

    public Long getStrengthDefenceHome() {
        return strengthDefenceHome;
    }

    public void setStrengthDefenceHome(Long strengthDefenceHome) {
        this.strengthDefenceHome = strengthDefenceHome;
    }

    public Long getStrengthDefenceAway() {
        return strengthDefenceAway;
    }

    public void setStrengthDefenceAway(Long strengthDefenceAway) {
        this.strengthDefenceAway = strengthDefenceAway;
    }

    @Override
    public String toString() {
        return "Team{" +
                "code=" + code +
                ", draw=" + draw +
                ", form=" + form +
                ", id=" + id +
                ", loss=" + loss +
                ", played=" + played +
                ", points=" + points +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", unavailable='" + unavailable + '\'' +
                ", strength=" + strength +
                ", teamDivision=" + teamDivision +
                ", win=" + win +
                ", strengthOverallHome=" + strengthOverallHome +
                ", strengthOverallAway=" + strengthOverallAway +
                ", strengthAttackHome=" + strengthAttackHome +
                ", strengthAttackAway=" + strengthAttackAway +
                ", strengthDefenceHome=" + strengthDefenceHome +
                ", strengthDefenceAway=" + strengthDefenceAway +
                '}';
    }
}
