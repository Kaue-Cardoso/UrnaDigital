package app.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Eleitor;
import app.Repository.EleitorRepository;

@Service
public class EleitorService {

	 @Autowired
	    private EleitorRepository eleitorRepository;

	    public Eleitor save(Eleitor eleitor) {
	        if (eleitor.getCpf() == null || eleitor.getEmail() == null) {
	            eleitor.setStatus("PENDENTE");
	        } else {
	            eleitor.setStatus("APTO");
	        }
	        return eleitorRepository.save(eleitor);
	    }
	    public Eleitor findById(Long id) {
	        Optional<Eleitor> eleitorOptional = eleitorRepository.findById(id);
	        return eleitorOptional.orElseThrow(() -> new RuntimeException("Eleitor não encontrado.")); // Lança exceção se não encontrado
	    }
	    
	    public Eleitor update(Long id, Eleitor eleitorAtualizado) {
	        Eleitor eleitorExistente = eleitorRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Eleitor não encontrado."));

	        eleitorExistente.setNomeCompleto(eleitorAtualizado.getNomeCompleto());
	        eleitorExistente.setCpf(eleitorAtualizado.getCpf());
	        eleitorExistente.setEmail(eleitorAtualizado.getEmail());
	        eleitorExistente.setProfissao(eleitorAtualizado.getProfissao());
	        eleitorExistente.setCelular(eleitorAtualizado.getCelular());
	        eleitorExistente.setTelefoneFixo(eleitorAtualizado.getTelefoneFixo());
	        eleitorExistente.setStatus(eleitorAtualizado.getStatus());

	        if (eleitorExistente.getCpf() == null || eleitorExistente.getEmail() == null) {
	            eleitorExistente.setStatus("PENDENTE");
	        } else {
	            eleitorExistente.setStatus("APTO");
	        }

	        return eleitorRepository.save(eleitorExistente);
	    }

	    public void delete(Long id) {
	        Eleitor eleitor = eleitorRepository.findById(id).orElseThrow(() -> new RuntimeException("Eleitor não encontrado."));
	        
	        // Lógica para mudar o status para INATIVO
	        if (!"VOTOU".equals(eleitor.getStatus())) {
	            eleitor.setStatus("INATIVO");
	            eleitorRepository.save(eleitor);
	        } else {
	            throw new RuntimeException("Usuário já votou. Não foi possível inativá-lo.");
	        }
	    }

	    public List<Eleitor> findAllAptos() {
	        return eleitorRepository.findByStatus("APTO");
	    }
	    public List<Eleitor> findAll() {
	        return eleitorRepository.findAll(); // Retorna todos os eleitores
	    }
}
