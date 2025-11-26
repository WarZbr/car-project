package projeto.pw3.carproject.conserto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsertoResumoDTO {

    private UUID id;
    private String dataEntrada;
    private String dataSaida;
    private String mecanicoNome;
    private String veiculoMarca;
    private String veiculoModelo;

}