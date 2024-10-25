package app.Controller;

import app.Entity.Eleitor;
import app.Service.EleitorService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/eleitores")
@CrossOrigin("*")
@Validated
public class EleitorController {

    @Autowired
    private EleitorService eleitorService;

    @PostMapping("/save")
    public ResponseEntity<Eleitor> create(@Valid @RequestBody Eleitor eleitor) {
        try {
            Eleitor novoEleitor = eleitorService.save(eleitor);
            return ResponseEntity.status(HttpStatus.OK).body(novoEleitor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Eleitor> findById(@PathVariable Long id) {
        try {
            Eleitor eleitor = eleitorService.findById(id);
            return ResponseEntity.ok(eleitor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Eleitor>> findAll() {
        try {
            List<Eleitor> eleitores = eleitorService.findAll();
            return ResponseEntity.ok(eleitores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/findAllAtivos")
    public ResponseEntity<List<Eleitor>> findAllAtivos() {
        try {
            List<Eleitor> eleitoresAptos= eleitorService.findAllAptos();
            return ResponseEntity.ok(eleitoresAptos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Eleitor> update(@Valid @PathVariable Long id, @RequestBody Eleitor eleitorAtualizado) {
        try {
            Eleitor eleitorAtualizadoRetornado = eleitorService.update(id, eleitorAtualizado);
            return ResponseEntity.status(HttpStatus.OK).body(eleitorAtualizadoRetornado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            eleitorService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
