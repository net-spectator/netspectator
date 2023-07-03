package org.net.logger.controllers;

import lombok.RequiredArgsConstructor;
import org.net.logger.entities.LogEvent;
import org.net.logger.mappers.DocumentMapper;
import org.net.logger.services.LoggerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs")
public class LogsController {
    private final LoggerService loggerService;
    private final DocumentMapper documentMapper;

    @GetMapping
    public List<LogEvent> getFilteredLogs(
            @RequestParam(value = "app",required = false) String app,
            @RequestParam(value = "level",required = false) String level,
            @RequestParam(value = "from",required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(value = "to",required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        List<LogEvent> results = loggerService.getFilteredLogs(app, level, from, to)
                .stream()
                .map(documentMapper::toLog)
                .collect(Collectors.toList());
        return results;
    }
}
