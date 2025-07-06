package me.redcare.popularity.scorer.scorer;

public enum ScorerType {
    DEFAULT("default"),
    Simple("simple");

    private final String value;

    ScorerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
