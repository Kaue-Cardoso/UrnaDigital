package app.UrnaDigital.Controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.Controller.VotoController;
import app.Entity.Apuracao;
import app.Entity.Candidato;
import app.Entity.Voto;
import app.Service.VotoService;

public class VotoControllerTest {

    @InjectMocks
    private VotoController votoController;

    @Mock
    private VotoService votoService;

    private Voto voto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        voto = new Voto();
        voto.setEleitorId(1L);
        voto.setCandidatoPrefeito(new Candidato(1L, "Carlinhos Maia","12345678911", 50, 1, "ATIVO", 0));
        voto.setCandidatoVereador(new Candidato(2L, "Bambino Pereira","12345678911", 12, 2, "ATIVO", 0));
    }

    @Test
    void Votar_Success() {
        String expectedHash = "12345-ABCDE";
        when(votoService.votar(any(Voto.class))).thenReturn(expectedHash);

        ResponseEntity<String> response = votoController.votar(voto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("li li li liiii (Som de Urna) Voto Confirmado 12345-ABCDE", response.getBody());
    }

    @Test
    void Votar_Error() {
        when(votoService.votar(any(Voto.class))).thenThrow(new RuntimeException("Erro de validação"));

        ResponseEntity<String> response = votoController.votar(voto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao votar: Erro de validação", response.getBody());
    }

    @Test
    void RealizarApuracao_Success() {
        Apuracao apuracao = new Apuracao();

        when(votoService.realizarApuracao()).thenReturn(apuracao);

        ResponseEntity<?> response = votoController.realizarApuracao();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apuracao, response.getBody());
    }

    @Test
    void RealizarApuracao_Error() {
        when(votoService.realizarApuracao()).thenThrow(new RuntimeException("Erro durante apuração"));

        ResponseEntity<?> response = votoController.realizarApuracao();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao realizar apuração: Erro durante apuração", response.getBody());
    }
}
