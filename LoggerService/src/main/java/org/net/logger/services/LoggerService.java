package org.net.logger.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.conversions.Bson;
import org.net.logger.entities.LogEvent;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

@Service
@RequiredArgsConstructor
public class LoggerService {

    private final MongoCollection<Document> mongoCollection;

    public void store(String logTime, Document document, String moduleName, String level) {
        document.append(LogEvent.APP, moduleName);
        document.append(LogEvent.LOG_TIME, LocalDateTime.parse(logTime));
        document.append(LogEvent.LEVEL, level);
        mongoCollection.insertOne(document);
    }

    public List<Document> getFilteredLogs(String moduleName, String level, LocalDateTime from, LocalDateTime to) {
        Bson filter = Filters.exists("_id");
        if (null != moduleName) {
            filter = eq(LogEvent.APP, moduleName);
        }
        if (null != level) {
            filter = Filters.and(filter, eq(LogEvent.LEVEL, level));
        }
        if (null != from) {
            filter = Filters.and(filter, gt(LogEvent.LOG_TIME, from));
        }
        if (null != to) {
            filter = Filters.and(filter, lt(LogEvent.LOG_TIME, to));
        }
        var result = new ArrayList<Document>();
        mongoCollection.find(filter).into(result);
        return result;
    }
}

