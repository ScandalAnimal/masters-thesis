package cz.vutbr.fit.maros.dip.model;

import java.io.Serializable;
import java.util.List;

public class MyTeam implements Serializable {

    private List<Chip> chips;
    private List<Pick> picks;
    private Transfers transfers;

    public List<Chip> getChips() {
        return chips;
    }

    public void setChips(List<Chip> chips) {
        this.chips = chips;
    }

    public List<Pick> getPicks() {
        return picks;
    }

    public void setPicks(List<Pick> picks) {
        this.picks = picks;
    }

    public Transfers getTransfers() {
        return transfers;
    }

    public void setTransfers(Transfers transfers) {
        this.transfers = transfers;
    }

    @Override
    public String toString() {
        return "MyTeam{" +
                "chips=" + chips +
                ", picks=" + picks +
                ", transfers=" + transfers +
                '}';
    }
}
