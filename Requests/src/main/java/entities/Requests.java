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
    private long request_user_id;

    @Column (name = "equipment_id")
    private long equipment_id;

    @Column (name = "executor_user_id")
    private long executor_user_id;

    @Column (name = "executor_comments")
    private String executor_comments;

    @ManyToOne
    @Column (name = "request_status_id")
    private RequestsStatus request_status;

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
        setRequest_user_id(builder.request_user_id);
        setEquipment_id(builder.equipment_id);
        setExecutor_user_id(builder.executor_user_id);
        setExecutor_comments(builder.executor_comments);
        setRequest_status(builder.request_status);
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
        private long request_user_id;
        private long equipment_id;
        private long executor_user_id;
        private String executor_comments;
        private RequestsStatus request_status;
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

        public Builder request_user_id(long request_user_id) {
            this.request_user_id = request_user_id;
            return this;
        }

        public Builder equipment_id(long equipment_id) {
            this.equipment_id = equipment_id;
            return this;
        }

        public Builder executor_user_id(long executor_user_id) {
            this.executor_user_id = executor_user_id;
            return this;
        }

        public Builder executor_comments(String executor_comments) {
            this.executor_comments = executor_comments;
            return this;
        }

        public Builder request_status(RequestsStatus request_status) {
            this.request_status = request_status;
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
