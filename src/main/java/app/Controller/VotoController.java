package app.Controller;

import app.Entity.Voto;
import app.Service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votacao")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping("/votar")
    public ResponseEntity<String> votar(@RequestBody Voto voto) {
        try {
            String hashComprovante = votoService.votar(voto);
            return ResponseEntity.status(HttpStatus.CREATED).body(hashComprovante);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao votar: " + e.getMessage());
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
