package org.net.logger.repositories;

import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.Document;
import org.net.logger.entities.LogEvent;
import org.net.logger.entities.LogId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogRepository {

    private final MongoCollection<Document> mongoCollection;

//    public LogId store(Document document, String moduleName, String level) {
//        document.append(LogEvent.APP, moduleName);
//        document.append(LogEvent.LOG_TIME, LocalDateTime.now());
//        document.append(LogEvent.LEVEL, level);
//        val result = mongoCollection.insertOne(document);
//        return new LogId(result.getInsertedId().asObjectId().getValue().toString());
//    }

    public void store(Document document, String moduleName, String level) {
        document.append(LogEvent.APP, moduleName);
        document.append(LogEvent.LOG_TIME, LocalDateTime.now());
        document.append(LogEvent.LEVEL, level);
        mongoCollection.insertOne(document);
    }
}

