package org.net.webcoreservice.entities;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "routers")
public class Routers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String routerTitle;

    @Column(name = "macAddress")
    private String routerMacAddress;

    @Column(name = "ipAddress")
    private String routerIpAddress;
}
