package app.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Candidato;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    List<Candidato> findByStatus(String status);
    boolean existsByIdAndFuncao(Long id, int funcao);

}
