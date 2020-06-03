package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TeamPlayer implements Serializable {

    private Long id;
    private Long sellingPrice;
    private Long purchasePrice;
    private Long nowCost;
    private String playerName;

    @Override
    public String toString() {
        return "TeamPlayer{" +
                "id=" + id +
                ", sellingPrice=" + sellingPrice +
                ", purchasePrice=" + purchasePrice +
                ", nowCost=" + nowCost +
                ", playerName='" + playerName + '\'' +
                '}';
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Long sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Long getNowCost() {
        return nowCost;
    }

    public void setNowCost(Long nowCost) {
        this.nowCost = nowCost;
    }

}
