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
	        // Definindo status como PENDENTE se faltar CPF ou e-mail
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

	    public List<Eleitor> findAllAtivos() {
	        return eleitorRepository.findByStatus("ATIVO");
	    }
	    public List<Eleitor> findAll() {
	        return eleitorRepository.findAll(); // Retorna todos os eleitores
	    }
}
