package app.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eleitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomeCompleto;

    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter exatamente 11 dígitos")
    private String cpf; // CPF pode ser nulo

    @NotBlank
    private String profissao;

    @NotBlank
    @Pattern(regexp = "\\d{9}", message = "Formato inserido deve ser xxxxxxxxx")
    private String celular;

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "Formato inserido deve ser xxxxxxxx")
    private String telefoneFixo;

    @Email
    @NotBlank
    private String email;

    private String status;
}
