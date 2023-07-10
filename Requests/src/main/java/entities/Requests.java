package entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Data
@Table(name = "requests")
@NoArgsConstructor
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private UUID reqId;
    @Column (name = "topic")
    private String topic;
    @Column (name = "description")
    private String description;
    @Column (name = "request_user_id")
    private UUID userId;
    @Column (name = "equipment_id")
    private UUID equipmentId;
    @Column (name = "executor_user_id")
    private UUID executorUserId;
    @Column (name = "executor_comments")
    private String executorComments;

//    @ManyToOne
    @Column (name = "request_status_id")
    private UUID reqStatusId;

    @CreationTimestamp
    @Column (name = "created_at")
    private LocalDateTime createdAt;
    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateAt;
}
