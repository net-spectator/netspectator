package repositories;

import entities.Requests;

@Repository
public interface RequestsRepository extends JpaRepository <Requests, Long> {
}
