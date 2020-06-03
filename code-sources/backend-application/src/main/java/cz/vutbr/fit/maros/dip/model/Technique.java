package cz.vutbr.fit.maros.dip.model;

public enum Technique {
    MAX("max"),
    TOTAL("total"),
    FORM("form");

    public final String label;

    Technique(String label) {
        this.label = label;
    }
}
