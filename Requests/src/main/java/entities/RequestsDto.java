package entities;

public class RequestsDto {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRequestUserId() {
        return requestUserId;
    }

    public void setRequest_userId(long request_userId) {
        this.requestUserId = request_userId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipment_id(long equipment_id) {
        this.equipmentId = equipment_id;
    }

    public long getExecutorUserId() {
        return executorUserId;
    }

    public void setExecutor_userId(long executor_userId) {
        this.executorUserId = executor_userId;
    }

    public String getExecutorComments() {
        return executorComments;
    }

    public void setExecutor_comments(String executor_comments) {
        this.executorComments = executor_comments;
    }

    public RequestsStatus getRequestsStatus() {
        return requestsStatus;
    }

    private long id;
    private String topic;
    private String description;
    private long requestUserId;
    private long equipmentId;
    private long executorUserId;
    private String executorComments;
    private RequestsStatus requestsStatus;

    public RequestsDto(long id, String topic, String description, long requestUserId, long equipmentId, long executorUserId, String executorComments, RequestsStatus requestsStatus) {
        this.id =  id;
        this.topic = topic;
        this.description = description;
        this.requestUserId = requestUserId;
        this.equipmentId = equipmentId;
        this.executorUserId = executorUserId;
        this.executorUserId = Long.parseLong(executorComments);
        this.executorComments = String.valueOf(requestsStatus);
    }

    public RequestsDto() {

    }
}
