package com.example.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "notification_type")
public class NotificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_title")
    private String typeTitle;

    @OneToMany(mappedBy = "typeId")
    private List<Groups> groupsList;
}
