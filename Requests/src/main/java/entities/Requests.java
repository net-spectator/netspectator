package entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private long request_status_id;

    @CreationTimestamp
    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column (name = "updated_at")
    private LocalDateTime updateAt;


    // Реализация Билдера с помощью Плагина
    private Requests(Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setPrice(builder.price);
        setCreatedAt(builder.createdAt);
        setUpdateAt(builder.updateAt);
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    private void Builder() {
    }

    public static Builder newBuilder(Requests copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.title = copy.getTitle();
        builder.price = copy.getPrice();
        builder.createdAt = copy.getCreatedAt();
        builder.updateAt = copy.getUpdateAt();
        return builder;
    }


    public static final class Builder {
        private long id;

        public Builder id(long id) {
            this.id = id;
            return this;
        }
    }
}
