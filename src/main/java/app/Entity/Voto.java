package app.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dataHora;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "prefeito_id")
    private Candidato candidatoPrefeito;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "vereador_id")
    private Candidato candidatoVereador;

    @Column(unique = true)
    private String hashComprovante;
    
    private Long eleitorId;
    
    public Voto() {
        this.dataHora = LocalDateTime.now();
        this.hashComprovante = gerarHashComprovante(); 
    }
    
    private String gerarHashComprovante() {
        return UUID.randomUUID().toString();
    }
}
