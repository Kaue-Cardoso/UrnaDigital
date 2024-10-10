package app.Entity;

import java.util.List;

import lombok.Data;

@Data
public class Apuracao {

    private int totalVotos;
    private List<Candidato> candidatosPrefeito;
    private List<Candidato> candidatosVereador;
}
