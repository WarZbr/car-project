package projeto.pw3.carproject.conserto.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Veiculo {

    private String marca;
    private String modelo;
    private String ano;
    private String cor;

}
