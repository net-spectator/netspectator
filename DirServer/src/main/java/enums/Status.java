package enums;

public enum Status {
    ONLINE("1"),
    OFFLINE("2");
    private final String status;

    Status(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return status;
    }
}
