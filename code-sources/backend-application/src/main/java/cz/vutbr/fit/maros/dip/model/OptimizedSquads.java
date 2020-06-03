package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import java.util.List;

public class OptimizedSquads implements Serializable {

    private List<OptimizedSquad> squads;

    public List<OptimizedSquad> getSquads() {
        return squads;
    }

    public void setSquads(List<OptimizedSquad> squads) {
        this.squads = squads;
    }

    @Override
    public String toString() {
        return "OptimizedSquads{"
                + "squads=" + squads
                + '}';
    }
}
