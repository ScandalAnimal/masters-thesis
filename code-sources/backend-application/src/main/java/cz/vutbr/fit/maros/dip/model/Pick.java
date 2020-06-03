package cz.vutbr.fit.maros.dip.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Pick implements Serializable {

    private Long element;
    private Long position;
    private Long sellingPrice;
    private Long multiplier;
    private Long purchasePrice;
    private String isCaptain;
    private String isViceCaptain;

    public Long getElement() {
        return element;
    }

    public void setElement(Long element) {
        this.element = element;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Long getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Long sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Long getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Long multiplier) {
        this.multiplier = multiplier;
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getIsCaptain() {
        return isCaptain;
    }

    public void setIsCaptain(String isCaptain) {
        this.isCaptain = isCaptain;
    }

    public String getIsViceCaptain() {
        return isViceCaptain;
    }

    public void setIsViceCaptain(String isViceCaptain) {
        this.isViceCaptain = isViceCaptain;
    }

    @Override
    public String toString() {
        return "Pick{" +
                "element=" + element +
                ", position=" + position +
                ", sellingPrice=" + sellingPrice +
                ", multiplier=" + multiplier +
                ", purchasePrice=" + purchasePrice +
                ", isCaptain='" + isCaptain + '\'' +
                ", isViceCaptain='" + isViceCaptain + '\'' +
                '}';
    }
}
