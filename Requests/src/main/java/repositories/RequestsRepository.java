package repositories;

import entities.Requests;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestsRepository extends JpaRepository<Requests, Long> {
    List<Requests> findAllByUsername(String username);
    Page<Requests> findAll(Specification<Requests> spec, PageRequest of);
}
