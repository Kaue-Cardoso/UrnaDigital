package app.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Apuracao;
import app.Entity.Candidato;
import app.Entity.Eleitor;
import app.Entity.Voto;
import app.Repository.VotoRepository;

@Service
public class VotoService {
	
    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private EleitorService eleitorService;

    public String votar(Voto voto) {
        if (!"APTO".equals(eleitorService.findById(voto.getEleitorId()).getStatus())) {
            throw new RuntimeException("Eleitor inapto para votação.");
        }

        if (!candidatoService.isCandidatoPrefeito(voto.getCandidatoPrefeito().getId())) {
            throw new RuntimeException("Candidato escolhido para prefeito é um candidato a vereador. Refaça a requisição!");
        }
        if (!candidatoService.isCandidatoVereador(voto.getCandidatoVereador().getId())) {
            throw new RuntimeException("Candidato escolhido para vereador é um candidato a prefeito. Refaça a requisição!");
        }

        voto.setDataHora(LocalDateTime.now());
        voto.setHashComprovante(UUID.randomUUID().toString());
        votoRepository.save(voto);
        
        Eleitor eleitor = eleitorService.findById(voto.getEleitorId());
        eleitor.setStatus("VOTOU");
        eleitorService.save(eleitor);

        return voto.getHashComprovante();
    }

    public Apuracao realizarApuracao() {
        List<Candidato> candidatosPrefeito = candidatoService.findAllAtivos().stream()
                .filter(c -> c.getFuncao() == 1) // Prefeito
                .collect(Collectors.toList());

        List<Candidato> candidatosVereador = candidatoService.findAllAtivos().stream()
                .filter(c -> c.getFuncao() == 2) // Vereador
                .collect(Collectors.toList());

        for (Candidato candidato : candidatosPrefeito) {
            int totalVotos = votoRepository.countByCandidatoPrefeito_Id(candidato.getId());
            candidato.setVotosTotais(totalVotos);
        }

        for (Candidato candidato : candidatosVereador) {
            int totalVotos = votoRepository.countByCandidatoVereador_Id(candidato.getId());
            candidato.setVotosTotais(totalVotos);
        }

        Collections.sort(candidatosPrefeito, (c1, c2) -> Integer.compare(c2.getVotosTotais(), c1.getVotosTotais()));
        Collections.sort(candidatosVereador, (c1, c2) -> Integer.compare(c2.getVotosTotais(), c1.getVotosTotais()));

        Apuracao apuracao = new Apuracao();
        apuracao.setCandidatosPrefeito(candidatosPrefeito);
        apuracao.setCandidatosVereador(candidatosVereador);
        return apuracao;
    }
}
