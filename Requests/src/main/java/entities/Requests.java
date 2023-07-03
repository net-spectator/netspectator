package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "requests")
@NoArgsConstructor
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private long id;

    @Column (name = "topic")
    private String topic;

    @Column (name = "description")
    private String description;

    @Column (name = "request_user_id")
    private long requestUserId;

    @Column (name = "equipment_id")
    private long equipmentId;

    @Column (name = "executor_user_id")
    private long executorUserId;

    @Column (name = "executor_comments")
    private String executorComments;

    @ManyToOne
    @Column (name = "request_status_id")
    private RequestsStatus requestsStatus;

    @CreationTimestamp
    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    private Requests(Builder builder) {
        setId(builder.id);
        setTopic(builder.topic);
        setDescription(builder.description);
        setRequestUserId(builder.requestUserId);
        setEquipmentId(builder.equipmentId);
        setExecutorUserId(builder.executorUserId);
        setExecutorComments(builder.executorComments);
        setRequestsStatus(builder.requestStatus);
        setCreatedAt(builder.createdAt);
        setUpdateAt(builder.updateAt);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private long id;
        private String topic;
        private String description;
        private long requestUserId;
        private long equipmentId;
        private long executorUserId;
        private String executorComments;
        private RequestsStatus requestStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updateAt;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder request_user_id(long requestUserId) {
            this.requestUserId = requestUserId;
            return this;
        }

        public Builder equipment_id(long equipmentId) {
            this.equipmentId = equipmentId;
            return this;
        }

        public Builder executor_user_id(long executorUserId) {
            this.executorUserId = executorUserId;
            return this;
        }

        public Builder executor_comments(String executorComments) {
            this.executorComments = executorComments;
            return this;
        }

        public Builder request_status(RequestsStatus requestsStatus) {
            this.requestStatus = requestsStatus;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updateAt(LocalDateTime updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public Requests build() {
            return new Requests(this);
        }
    }
}
