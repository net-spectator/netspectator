package org.net.logger.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public final class LogEvent {
    public static final String LOG_ID = "_id";
    public static final String LOG_TIME = "_createDate";
    public static final String APP = "_app";
    public static final String LEVEL = "_level";

    private Map<String, String> eventContent;

    public LogEvent() {
        this.eventContent = new HashMap<>();
    }
}