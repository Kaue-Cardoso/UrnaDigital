package app.Controller;

import app.Entity.Candidato;
import app.Entity.Eleitor;
import app.Service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @PostMapping("/save")
    public ResponseEntity<Candidato> create(@RequestBody Candidato candidato) {
        try {
            Candidato novoCandidato = candidatoService.save(candidato);
            return ResponseEntity.status(HttpStatus.OK).body(novoCandidato);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Candidato> findById(@PathVariable Long id) {
        try {
            Candidato candidato = candidatoService.findById(id);
            return ResponseEntity.ok(candidato);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Candidato>> findAll() {
        try {
            List<Candidato> candidatos = candidatoService.findAllAtivos();
            return ResponseEntity.ok(candidatos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            candidatoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
