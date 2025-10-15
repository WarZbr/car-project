package projeto.pw3.carproject.conserto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsertoResumoDTO {

    private long id;
    private String dataEntrada;
    private String dataSaida;
    private String mecanicoNome;
    private String veiculoMarca;
    private String veiculoModelo;

}
