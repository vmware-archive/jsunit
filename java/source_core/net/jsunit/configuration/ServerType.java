package net.jsunit.configuration;

public enum ServerType {
    SERVER("Server", false),
    AGGREGATE("Aggregate", true);

    private String displayName;
    private boolean isAggregate;

    private ServerType(String displayName, boolean isAggregate) {
        this.displayName = displayName;
        this.isAggregate = isAggregate;
    }

    public boolean isAggregate() {
        return isAggregate;
    }

    public String getDisplayName() {
        return displayName;
    }

}
