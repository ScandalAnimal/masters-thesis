package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Chip implements Serializable {

    private String statusForEntry;
    private List<Long> playedByEntry;
    private String name;
    private Long number;
    private Long startEvent;
    private Long stopEvent;
    private String chipType;

    public String getStatusForEntry() {
        return statusForEntry;
    }

    public void setStatusForEntry(String statusForEntry) {
        this.statusForEntry = statusForEntry;
    }

    public List<Long> getPlayedByEntry() {
        return playedByEntry;
    }

    public void setPlayedByEntry(List<Long> playedByEntry) {
        this.playedByEntry = playedByEntry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(Long startEvent) {
        this.startEvent = startEvent;
    }

    public Long getStopEvent() {
        return stopEvent;
    }

    public void setStopEvent(Long stopEvent) {
        this.stopEvent = stopEvent;
    }

    public String getChipType() {
        return chipType;
    }

    public void setChipType(String chipType) {
        this.chipType = chipType;
    }

    @Override
    public String toString() {
        return "Chip{" +
                "statusForEntry='" + statusForEntry + '\'' +
                ", playedByEntry=" + playedByEntry +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", startEvent=" + startEvent +
                ", stopEvent=" + stopEvent +
                ", chipType='" + chipType + '\'' +
                '}';
    }
}
