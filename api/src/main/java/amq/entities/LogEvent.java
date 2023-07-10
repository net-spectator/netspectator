package amq.entities;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public final class LogEvent {
    private Map<String, String> eventContent;
    public LogEvent(String className, String message) {
        this.eventContent = new HashMap<>();
        this.eventContent.put("Class", className);
        this.eventContent.put("Message", message);
    }

    public LogEvent(String className, String message, Throwable t) {
        this.eventContent = new HashMap<>();
        this.eventContent.put("Class", className);
        this.eventContent.put("Message", message);
        this.eventContent.put("Stacktrace", t.getMessage());
    }
}