package app.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomeCompleto;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter exatamente 11 dígitos")
    private String cpf; // CPF obrigatório

    //@Column(nullable = false, unique = true)
    private Integer numero; // Único

    @NotNull
    @Min(1)
    @Max(2)
    private Integer funcao;

    // Status agora é String
    private String status;

    @Transient
    private int votosTotais; // Votos são calculados, não persistidos
}
