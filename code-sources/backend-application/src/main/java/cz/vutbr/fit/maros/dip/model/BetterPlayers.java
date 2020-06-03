package cz.vutbr.fit.maros.dip.model;

import java.util.List;

public class BetterPlayers {

    private List<PlayerStats> toRemove;
    private List<PlayerStats> toAdd;

    public BetterPlayers(List<PlayerStats> toRemove, List<PlayerStats> toAdd) {
        this.toRemove = toRemove;
        this.toAdd = toAdd;
    }

    public List<PlayerStats> getToRemove() {
        return toRemove;
    }

    public void setToRemove(List<PlayerStats> toRemove) {
        this.toRemove = toRemove;
    }

    public List<PlayerStats> getToAdd() {
        return toAdd;
    }

    public void setToAdd(List<PlayerStats> toAdd) {
        this.toAdd = toAdd;
    }

    @Override
    public String toString() {
        return "BetterPlayers{" +
                "toRemove=" + toRemove +
                ", toAdd=" + toAdd +
                '}';
    }
}
