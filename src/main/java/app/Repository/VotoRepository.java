package app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.Entity.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    int countByCandidatoPrefeito_Id(Long candidatoPrefeitoId);

    int countByCandidatoVereador_Id(Long candidatoVereadorId);
}
