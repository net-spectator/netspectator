package com.example.notificationservice.Repository;

import com.example.notificationservice.entity.Groups;
import com.example.notificationservice.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {

    List<Groups> findAllByTypeId(NotificationType typeId);
}
