package app.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Candidato;
import app.Repository.CandidatoRepository;

@Service
public class CandidatoService {
	  @Autowired
	    private CandidatoRepository candidatoRepository;

	    public Candidato save(Candidato candidato) {
	        candidato.setStatus("ATIVO");
	        return candidatoRepository.save(candidato);
	    }
	    public Candidato findById(Long id) {
	        return candidatoRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidato não encontrado."));
	    }

	    public void delete(Long id) {
	        Candidato candidato = candidatoRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidato não encontrado."));
	        candidato.setStatus("INATIVO");
	        candidatoRepository.save(candidato);
	    }

	    public List<Candidato> findAllAtivos() {
	        return candidatoRepository.findByStatus("ATIVO");
	    }

	    public boolean isCandidatoPrefeito(Long id) {
	        return candidatoRepository.existsByIdAndFuncao(id, 1); // 1 para Prefeito
	    }

	    public boolean isCandidatoVereador(Long id) {
	        return candidatoRepository.existsByIdAndFuncao(id, 2); // 2 para Vereador
	    }
}
