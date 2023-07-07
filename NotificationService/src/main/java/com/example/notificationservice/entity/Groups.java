package com.example.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
@Table(name = "gpoups")
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_uuid")
    private UUID userUuid;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private NotificationType typeId;
}
