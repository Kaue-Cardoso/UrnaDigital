package app.UrnaDigital.Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.Entity.Candidato;
import app.Repository.CandidatoRepository;
import app.Service.CandidatoService;

public class CandidatoServiceTest {

    @InjectMocks
    private CandidatoService candidatoService;

    @Mock
    private CandidatoRepository candidatoRepository;

    private Candidato candidatoAtivo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        candidatoAtivo = new Candidato(1L, "Carlos Silva","12345678911", 12, 1, "ATIVO", 0);
    }

    @Test
    void Save() {
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidatoAtivo);

        Candidato savedCandidato = candidatoService.save(candidatoAtivo);

        assertEquals(candidatoAtivo.getNomeCompleto(), savedCandidato.getNomeCompleto());
        assertEquals("ATIVO", savedCandidato.getStatus());
    }

    @Test
    void FindById_Success() {
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidatoAtivo));

        Candidato foundCandidato = candidatoService.findById(1L);

        assertEquals(candidatoAtivo, foundCandidato);
    }

    @Test
    void FindById_Failure() {
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            candidatoService.findById(1L);
        });

        assertEquals("Candidato não encontrado.", exception.getMessage());
    }

    @Test
    void Delete_Success() {
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.of(candidatoAtivo));
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidatoAtivo);

        candidatoService.delete(1L);

        verify(candidatoRepository).save(candidatoAtivo);
        assertEquals("INATIVO", candidatoAtivo.getStatus());
    }

    @Test
    void Delete_Failure() {
        when(candidatoRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            candidatoService.delete(1L);
        });

        assertEquals("Candidato não encontrado.", exception.getMessage());
    }

    @Test
    void FindAllAtivos() {
        when(candidatoRepository.findByStatus("ATIVO")).thenReturn(Arrays.asList(candidatoAtivo));

        var ativos = candidatoService.findAllAtivos();

        assertEquals(1, ativos.size());
        assertEquals(candidatoAtivo, ativos.get(0));
    }

    @Test
    void CandidatoPrefeito_True() {
        when(candidatoRepository.existsByIdAndFuncao(anyLong(), anyInt())).thenReturn(true);

        boolean result = candidatoService.isCandidatoPrefeito(1L);

        assertTrue(result);
    }

    @Test
    void CandidatoPrefeito_False() {
        when(candidatoRepository.existsByIdAndFuncao(anyLong(), anyInt())).thenReturn(false);

        boolean result = candidatoService.isCandidatoPrefeito(1L);

        assertFalse(result);
    }

    @Test
    void CandidatoVereador_True() {
        when(candidatoRepository.existsByIdAndFuncao(anyLong(), anyInt())).thenReturn(true);

        boolean result = candidatoService.isCandidatoVereador(1L);

        assertTrue(result);
    }

    @Test
    void CandidatoVereador_False() {
        when(candidatoRepository.existsByIdAndFuncao(anyLong(), anyInt())).thenReturn(false);

        boolean result = candidatoService.isCandidatoVereador(1L);

        assertFalse(result);
    }
}
