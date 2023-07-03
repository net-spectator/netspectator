package enums;

public enum Status {
    OFFLINE(0),
    ONLINE(1);
    private final Integer status;

    Status(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status.toString();
    }
}
