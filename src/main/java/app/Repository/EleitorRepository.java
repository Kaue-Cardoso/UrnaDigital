package app.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Eleitor;

public interface EleitorRepository extends JpaRepository<Eleitor, Long> {
    List<Eleitor> findByStatus(String status);
}
