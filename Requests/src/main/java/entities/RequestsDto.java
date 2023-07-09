package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class RequestsDto {
    private UUID reqId;
    private String topic;
    private String description;
    private UUID userId;
    private UUID equipmentId;
    private UUID executorUserId;
    private String executorComments;
    private UUID reqStatusId;
}
