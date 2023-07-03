package org.net.logger.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String logTime;
    private LogEvent logEvent;
    private String moduleName;
    private String level;
}