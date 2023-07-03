package com.example.notificationservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "notification_type")
public class NotificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "error_type")
    private String errorType;

    @OneToMany(mappedBy = "typeId")
    private List<Groups> groupsList;
}
