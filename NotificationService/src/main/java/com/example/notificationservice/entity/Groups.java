package com.example.notificationservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "gpoups")
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_uuid")
    private String userUuid;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private NotificationType typeId;
}
