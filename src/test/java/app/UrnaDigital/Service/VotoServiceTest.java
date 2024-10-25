package app.UrnaDigital.Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.Entity.Apuracao;
import app.Entity.Candidato;
import app.Entity.Eleitor;
import app.Entity.Voto;
import app.Repository.VotoRepository;
import app.Service.CandidatoService;
import app.Service.EleitorService;
import app.Service.VotoService;

public class VotoServiceTest {

    @InjectMocks
    private VotoService votoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private EleitorService eleitorService;

    private Candidato candidatoPrefeito;
    private Candidato candidatoVereador;
    private Eleitor eleitor;
    private Voto voto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        candidatoPrefeito = new Candidato(1L, "Carlinhos Maia","12345678911", 50, 1, "ATIVO", 0);
        candidatoVereador = new Candidato(2L, "Bambino Pereira","12345678911", 12, 2, "ATIVO", 0);
        eleitor = new Eleitor(1L, "João Silva", "12345678900","Pedreiro","123456789", "12345678" , "joao@example.com", "APTO");
        
        voto = new Voto();
        voto.setEleitorId(eleitor.getId());
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);
    }

    @Test
    void testVotar_Success() {
        when(eleitorService.findById(anyLong())).thenReturn(eleitor);
        when(candidatoService.isCandidatoPrefeito(anyLong())).thenReturn(true);
        when(candidatoService.isCandidatoVereador(anyLong())).thenReturn(true);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);
        
        String hashComprovante = votoService.votar(voto);
        
        assertNotNull(hashComprovante);
        assertEquals(voto.getHashComprovante(), hashComprovante);
        assertEquals("VOTOU", eleitor.getStatus());
        verify(votoRepository).save(voto);
        verify(eleitorService).save(eleitor);
    }

    @Test
    void testVotar_EleitorInapto() {
        eleitor.setStatus("INATIVO"); // Mudando o status do eleitor para inapto
        when(eleitorService.findById(anyLong())).thenReturn(eleitor);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(voto);
        });

        assertEquals("Eleitor inapto para votação.", exception.getMessage());
    }

    @Test
    void testVotar_CandidatoPrefeitoIncorreto() {
        when(eleitorService.findById(anyLong())).thenReturn(eleitor);
        when(candidatoService.isCandidatoPrefeito(anyLong())).thenReturn(false);
        when(candidatoService.isCandidatoVereador(anyLong())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(voto);
        });

        assertEquals("Candidato escolhido para prefeito é um candidato a vereador. Refaça a requisição!", exception.getMessage());
    }

    @Test
    void testVotar_CandidatoVereadorIncorreto() {
        when(eleitorService.findById(anyLong())).thenReturn(eleitor);
        when(candidatoService.isCandidatoPrefeito(anyLong())).thenReturn(true);
        when(candidatoService.isCandidatoVereador(anyLong())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(voto);
        });

        assertEquals("Candidato escolhido para vereador é um candidato a prefeito. Refaça a requisição!", exception.getMessage());
    }

    @Test
    void testRealizarApuracao() {
        Candidato candidatoAtivoPrefeito = new Candidato(1L, "Carlinhos Maia","12345678911", 50, 1, "ATIVO", 0);
        Candidato candidatoAtivoVereador = new Candidato(2L, "Bambino Pereira","12345678911", 12, 2, "ATIVO", 0);

        when(candidatoService.findAtivos()).thenReturn(Arrays.asList(candidatoAtivoPrefeito, candidatoAtivoVereador));
        when(votoRepository.countByCandidatoPrefeito_Id(candidatoAtivoPrefeito.getId())).thenReturn(10);
        when(votoRepository.countByCandidatoVereador_Id(candidatoAtivoVereador.getId())).thenReturn(5);

        Apuracao apuracao = votoService.realizarApuracao();

        assertNotNull(apuracao);
        assertEquals(1, apuracao.getCandidatosPrefeito().size());
        assertEquals(1, apuracao.getCandidatosVereador().size());
        assertEquals(10, apuracao.getCandidatosPrefeito().get(0).getVotosTotais());
        assertEquals(5, apuracao.getCandidatosVereador().get(0).getVotosTotais());
    }
}
