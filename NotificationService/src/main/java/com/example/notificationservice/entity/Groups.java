package com.example.notificationservice.entity;

import lombok.Data;

import javax.persistence.*;
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
