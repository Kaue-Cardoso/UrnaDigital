package app.UrnaDigital.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.Controller.EleitorController;
import app.Entity.Eleitor;
import app.Service.EleitorService;

class EleitorControllerTest {

    @Mock
    private EleitorService eleitorService;

    @InjectMocks
    private EleitorController eleitorController;

    private Eleitor eleitorAtivo;
    private Eleitor eleitorInativo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        eleitorAtivo = new Eleitor(1L, "Kaue Cardoso", "12345678901", "Professor", "987654321", "11223344", "joao@example.com", "ATIVO");
        eleitorInativo = new Eleitor(2L, "Kael Cardoso", "98765432109", "Engenheira", "912345678", "99887766", "", "INATIVO");
    }

    @Test
    void CreateEleitor_Success() {
        when(eleitorService.save(any(Eleitor.class))).thenReturn(eleitorAtivo);

        ResponseEntity<Eleitor> response = eleitorController.create(eleitorAtivo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eleitorAtivo, response.getBody());
    }

    @Test
    void CreateEleitor_Failure() {
        when(eleitorService.save(any(Eleitor.class))).thenThrow(new RuntimeException("Erro ao salvar eleitor"));

        ResponseEntity<Eleitor> response = eleitorController.create(eleitorAtivo);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void FindById_Success() {
        when(eleitorService.findById(1L)).thenReturn(eleitorAtivo);

        ResponseEntity<Eleitor> response = eleitorController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eleitorAtivo, response.getBody());
    }

    @Test
    void FindById_Failure() {
        when(eleitorService.findById(anyLong())).thenThrow(new RuntimeException("Eleitor não encontrado"));

        ResponseEntity<Eleitor> response = eleitorController.findById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void FindAll_Success() {
        List<Eleitor> eleitores = Arrays.asList(eleitorAtivo, eleitorInativo);
        when(eleitorService.findAll()).thenReturn(eleitores);

        ResponseEntity<List<Eleitor>> response = eleitorController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eleitores, response.getBody());
    }

    @Test
    void FindAllAtivos_Success() {
        List<Eleitor> eleitoresAtivos = Arrays.asList(eleitorAtivo);
        when(eleitorService.findAllAptos()).thenReturn(eleitoresAtivos);

        ResponseEntity<List<Eleitor>> response = eleitorController.findAllAtivos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eleitoresAtivos, response.getBody());
    }

    @Test
    void DeleteEleitor_Success() {
        doNothing().when(eleitorService).delete(1L);

        ResponseEntity<Void> response = eleitorController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void DeleteEleitor_Failure() {
        doThrow(new RuntimeException("Erro ao deletar eleitor")).when(eleitorService).delete(1L);

        ResponseEntity<Void> response = eleitorController.delete(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
