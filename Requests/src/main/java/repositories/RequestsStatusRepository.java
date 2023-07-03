package repositories;

import entities.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestsStatusRepository extends JpaRepository<Requests, Long> {
}
