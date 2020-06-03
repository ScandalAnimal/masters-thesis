package cz.vutbr.fit.maros.dip.model;

import java.io.Serializable;


public class Transfers implements Serializable {

    private Long cost;
    private String status;
    private Long limit;
    private Long made;
    private Long bank;
    private Long value;

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Long getMade() {
        return made;
    }

    public void setMade(Long made) {
        this.made = made;
    }

    public Long getBank() {
        return bank;
    }

    public void setBank(Long bank) {
        this.bank = bank;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Transfers{" +
                "cost=" + cost +
                ", status='" + status + '\'' +
                ", limit=" + limit +
                ", made=" + made +
                ", bank=" + bank +
                ", value=" + value +
                '}';
    }
}
