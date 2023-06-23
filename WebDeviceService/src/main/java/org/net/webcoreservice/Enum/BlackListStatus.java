package org.net.webcoreservice.Enum;

public enum BlackListStatus {
    ENABLE(0),
    DISABLE(1);

    private final int status;

    BlackListStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
