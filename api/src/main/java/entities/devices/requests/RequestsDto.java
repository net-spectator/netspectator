package entities.devices.requests;

import entities.RequestsStatus;

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

    public long getRequest_user_id() {
        return request_user_id;
    }

    public void setRequest_user_id(long request_user_id) {
        this.request_user_id = request_user_id;
    }

    public long getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(long equipment_id) {
        this.equipment_id = equipment_id;
    }

    public long getExecutor_user_id() {
        return executor_user_id;
    }

    public void setExecutor_user_id(long executor_user_id) {
        this.executor_user_id = executor_user_id;
    }

    public String getExecutor_comments() {
        return executor_comments;
    }

    public void setExecutor_comments(String executor_comments) {
        this.executor_comments = executor_comments;
    }

    public RequestsStatus getRequest_status() {
        return request_status;
    }

    private long id;
    private String topic;
    private String description;
    private long request_user_id;
    private long equipment_id;
    private long executor_user_id;
    private String executor_comments;
    private RequestsStatus request_status;

    public RequestsDto(long id, String topic, String description, long request_user_id, long equipment_id, long executor_user_id, String executor_comments,RequestsStatus request_status) {
        this.id =  id;
        this.topic = topic;
        this.description = description;
        this.request_user_id = request_user_id;
        this.equipment_id = equipment_id;
        this.executor_user_id = executor_user_id;
        this.executor_user_id = Long.parseLong(executor_comments);
        this.executor_comments = String.valueOf(request_status);
    }

    public RequestsDto() {

    }
}
