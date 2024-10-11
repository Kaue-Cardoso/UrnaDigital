package app.UrnaDigital.Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import app.Entity.Eleitor;
import app.Repository.EleitorRepository;
import app.Service.EleitorService;

public class EleitorServiceTest {

    @InjectMocks
    private EleitorService eleitorService;

    @Mock
    private EleitorRepository eleitorRepository;

    private Eleitor eleitorAtivo;
    private Eleitor eleitorPendente;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        eleitorAtivo = new Eleitor(1L, "João Silva", "12345678900","Pedreiro","123456789", "12345678" , "joao@example.com", "APTO");
        eleitorPendente = new Eleitor(2L, "Maria Souza", "12345678900","Do Job","123456789", "12345678" , null, "PENDENTE");
    }

    @Test
    void Save_EleitorAtivo() {
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitorAtivo);

        Eleitor savedEleitor = eleitorService.save(eleitorAtivo);

        assertEquals(eleitorAtivo.getNomeCompleto(), savedEleitor.getNomeCompleto());
        assertEquals("APTO", savedEleitor.getStatus());
    }

    @Test
    void Save_EleitorPendente() {
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitorPendente);

        Eleitor savedEleitor = eleitorService.save(eleitorPendente);

        assertEquals(eleitorPendente.getNomeCompleto(), savedEleitor.getNomeCompleto());
        assertEquals("PENDENTE", savedEleitor.getStatus());
    }

    @Test
    void FindById_Success() {
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitorAtivo));

        Eleitor foundEleitor = eleitorService.findById(1L);

        assertEquals(eleitorAtivo, foundEleitor);
    }

    @Test
    void FindById_Failure() {
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eleitorService.findById(1L);
        });

        assertEquals("Eleitor não encontrado.", exception.getMessage());
    }

    @Test
    void Delete_Success() {
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitorAtivo));
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitorAtivo);

        eleitorService.delete(1L);

        verify(eleitorRepository).save(eleitorAtivo);
        assertEquals("INATIVO", eleitorAtivo.getStatus());
    }

    @Test
    void Delete_Failure_NotFound() {
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eleitorService.delete(1L);
        });

        assertEquals("Eleitor não encontrado.", exception.getMessage());
    }

    @Test
    void Delete_Failure_UserAlreadyVoted() {
        eleitorAtivo.setStatus("VOTOU");
        when(eleitorRepository.findById(anyLong())).thenReturn(Optional.of(eleitorAtivo));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eleitorService.delete(1L);
        });

        assertEquals("Usuário já votou. Não foi possível inativá-lo.", exception.getMessage());
    }

    @Test
    void FindAllAptos() {
        when(eleitorRepository.findByStatus("APTO")).thenReturn(Arrays.asList(eleitorAtivo));

        var aptos = eleitorService.findAllAptos();

        assertEquals(1, aptos.size());
        assertEquals(eleitorAtivo, aptos.get(0));
    }

    @Test
    void FindAll() {
        when(eleitorRepository.findAll()).thenReturn(Arrays.asList(eleitorAtivo, eleitorPendente));

        var todos = eleitorService.findAll();

        assertEquals(2, todos.size());
        assertEquals(eleitorAtivo, todos.get(0));
        assertEquals(eleitorPendente, todos.get(1));
    }
}
