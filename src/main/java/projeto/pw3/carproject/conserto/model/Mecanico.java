package projeto.pw3.carproject.conserto.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Mecanico {

    private String nome;
    private Integer anosExperiencia;

}
