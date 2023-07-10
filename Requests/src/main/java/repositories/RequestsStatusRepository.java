package repositories;

import entities.Requests;
import entities.RequestsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestsStatusRepository extends JpaRepository<RequestsStatus, Long> {
    Optional<RequestsStatus> findByTitle(String title);
}
