package app.UrnaDigital.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
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

import app.Controller.CandidatoController;
import app.Entity.Candidato;
import app.Service.CandidatoService;

class CandidatoControllerTest {

    @InjectMocks
    private CandidatoController candidatoController;

    @Mock
    private CandidatoService candidatoService;

    private Candidato candidatoAtivo;
    private Candidato candidatoInativo;
    private List<Candidato> listaCandidatos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        candidatoAtivo = new Candidato();
        candidatoAtivo.setId(1L);
        candidatoAtivo.setNomeCompleto("Kaue Cardoso");
        candidatoAtivo.setCpf("11111111111");
        candidatoAtivo.setNumero(10);
        candidatoAtivo.setFuncao(1);
        candidatoAtivo.setStatus("ATIVO");

        candidatoInativo = new Candidato();
        candidatoInativo.setId(2L);
        candidatoInativo.setNomeCompleto("Maria Souza");
        candidatoInativo.setCpf("22222222222");
        candidatoInativo.setNumero(11);
        candidatoInativo.setFuncao(2);
        candidatoInativo.setStatus("INATIVO");

        listaCandidatos = Arrays.asList(candidatoAtivo, candidatoInativo);

        when(candidatoService.save(any(Candidato.class))).thenReturn(candidatoAtivo);
        when(candidatoService.findById(1L)).thenReturn(candidatoAtivo);
        when(candidatoService.findAllAtivos()).thenReturn(Arrays.asList(candidatoAtivo));
        doNothing().when(candidatoService).delete(1L);
    }

    @Test
    void CreateCandidato_Success() {
        ResponseEntity<Candidato> response = candidatoController.create(candidatoAtivo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidatoAtivo, response.getBody());
    }

    @Test
    void CreateCandidato_Failure() {
        when(candidatoService.save(any(Candidato.class))).thenThrow(new RuntimeException("Erro ao salvar candidato"));

        ResponseEntity<Candidato> response = candidatoController.create(candidatoAtivo);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void FindCandidato_Success() {
        ResponseEntity<Candidato> response = candidatoController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidatoAtivo, response.getBody());
    }

    @Test
    void FindCandidato_Failure() {
        when(candidatoService.findById(1L)).thenThrow(new RuntimeException("Candidato não encontrado"));

        ResponseEntity<Candidato> response = candidatoController.findById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void FindAllAtivos_Success() {
        ResponseEntity<List<Candidato>> response = candidatoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(candidatoAtivo, response.getBody().get(0));
    }

    @Test
    void FindAllAtivos_Failure() {
        when(candidatoService.findAllAtivos()).thenThrow(new RuntimeException("Erro ao buscar candidatos"));

        ResponseEntity<List<Candidato>> response = candidatoController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void DeleteCandidato_Success() {
        ResponseEntity<Void> response = candidatoController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void DeleteCandidato_Failure() {
        doThrow(new RuntimeException("Erro ao deletar candidato")).when(candidatoService).delete(1L);

        ResponseEntity<Void> response = candidatoController.delete(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
