package app.Controller;

import app.Entity.Voto;
import app.Service.VotoService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("app/votos")
@CrossOrigin("*")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping("/votar")
    public ResponseEntity<Map<String, String>> votar(@RequestBody Voto voto) {
        Map<String, String> response = new HashMap<>();
        try {
            String hashComprovante = votoService.votar(voto);
            response.put("message", "li li li liiii (Som de Urna) Voto Confirmado " + hashComprovante);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Erro ao votar: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/apuracao")
    public ResponseEntity<?> realizarApuracao() {
        try {
            var apuracao = votoService.realizarApuracao();
            return ResponseEntity.ok(apuracao);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao realizar apuração: " + e.getMessage());
        }
    }
}
