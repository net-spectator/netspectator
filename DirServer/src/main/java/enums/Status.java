package enums;

public enum Status {
    ONLINE(1),
    OFFLINE(2);
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
